<%
	ApplicationVersion = "1.0" ' 版本
%>

<%
	' 初始化
	function create()
		dim i
		if not ApplicationVersion = Application("version") then		
			Application.Contents.RemoveAll() ' 清空
			Application("choices") = Array("星期一","星期二","星期三","星期四","星期五","星期六","星期日","全部") ' 日期
			Application("backgrounds") = Array("#00fa9a","#48d1cc","#66cdaa", _
					"#3cb371","#9370db","#7b68ee","#ba55d3","#c71585") ' 背景颜色
			Application("sections") = Array("第一大节","第二大节","第三大节","第四大节","第五大节","第六大节") ' 节次
			Application("course_fields") = Array("id","name","teacher") ' 课程字段
			Application("timetable_fields") = Array("day","section","course","week","place", _
					"id","teacher") ' 课程安排字段，course、id、teacher字段依赖于课程
			Application("term_start") = "2018/3/5" ' 学期开始日期，选择星期一的那天
			Application("max_week") = 16 ' 学期的周数
			initData()
			
			dim fileObj
			dim temp
			set fileObj = Server.CreateObject("Scripting.FileSystemObject")
			if not fileObj.folderExists(Server.MapPath("visitor")) then ' 访客文件夹不存在则创建
			    fileObj.createFolder(Server.MapPath("visitor"))
			end if
			set temp = fileObj.openTextFile(Server.MapPath("visitor/count.txt"),1,true) ' 以读的方式打开访问量文件，不存在则自动创建
			
			' 读取访问量
			if temp.atEndOfStream then
				Application("count") = 0
			else
				Application("count") = int(temp.readAll())
			end if
			
			temp.close()
			
			Application("version") = ApplicationVersion
		elseif datediff("s",Application("courses_modified") , getLastModified("data/courses.xml"))<>0 or _
				datediff("s",Application("timetable_modified") , getLastModified("data/timetable.xml"))<>0 or _
				datediff("s",Application("style_modified") , getLastModified("css/style.css"))<>0 then
			initData()
			for i = 0 to ubound(Application("choices"))
				Application.Contents.Remove(Application("choices")(i))
			next
			Application.Contents.Remove("date")
			Application.Contents.Remove("today")
		end if
		
		' 选择，默认是全部
		Session("choice_index") = 7
		Session("background_color")=Application("backgrounds")(7)
		
		' 根据链接判断选择项，不存在则为默认
		for i = 0 to ubound(Application("choices"))
			if Request.QueryString("choice") = Application("choices")(i) then
				Session("choice_index") = i
				Session("background_color")=Application("backgrounds")(i)
				exit for
			end if
		next
		
		' 访问量增加
		if isempty(Session("visited")) then
			visit()
		end if
		
		Session("time") = now() ' 当前时间
		setRefreshTimer() ' 设置第二天刷新页面
	end function
%>

<%
	' 访问量增加
	function visit()
		dim fileObj
		dim temp
		set fileObj = Server.CreateObject("Scripting.FileSystemObject")
		
		Application("count") = Application("count") + 1 ' 访问量增1
		
		' 将访问量写入文件
		set temp = fileObj.OpenTextFile(Server.MapPath("visitor/count.txt"),2,true)
		temp.write(Application("count"))
		temp.close()
		
		' 将访客信息写入文件
		set temp = fileObj.OpenTextFile(Server.MapPath("visitor/info.txt"),8,true)
		temp.writeline()
		temp.writeline(Application("count"))
		temp.writeline("time:	" & now)
		temp.writeline("addr:	" & Request.ServerVariables("remote_addr"))
		temp.writeline("agent:	" & Request.ServerVariables("http_user_agent"))
		temp.close()
		
		Session("visited") = true
	end function
%>

<%
	' 初始化课程和课程安排数据
	function initData()
		Application("courses_modified") = getLastModified("data/courses.xml")
		Application("timetable_modified") = getLastModified("data/timetable.xml")
		Application("style_modified") = getLastModified("css/style.css")
		Application.Contents.Remove("courses")
		Application.Contents.Remove("timetable")
		Application.Contents.Remove("unscheduled")
		Application.Contents.Remove("timetable_index")
		initCourses()
		if not isempty(Application("courses")) then
			initTimetable()
		end if
	end function
%>

<%
	' 第二天刷新页面
	function setRefreshTimer()
		' js脚本
		Response.Write("<script type='text/javascript'>" & _
			"let from = new Date('"&Session("time")&" GMT+8');" & _
			"from.setTime(from.getTime() + 8 * 60 * 60 * 1000);" & _
			"let to =  new Date(from.getTime()+24*60*60*1000);" & _
			"to.setUTCHours(0,0,0,0);" & _
			"setTimeout(function(){" & _
				"location.reload(true);" & _
			"},to.getTime()-from.getTime());" & _
			"</script>")
	end function
%>

<%
	' 创建选项链接
	function createChoiceLinks()
		dim i
		dim s,cls
		for i = 0 to ubound(Application("choices"))
			s = server.urlencode(Application("choices")(i))
			
			' 被选中的选项链接和未被选中的样式不同
			if Application("choices")(i)=Application("choices")(Session("choice_index")) then
				cls = "checked"
			else
				cls = "unchecked"
			end if
			
			Response.Write("<p><a class="& cls & _
				" href='default.asp?choice="& s & _
				"' onclick='choose(id)'>" & Application("choices")(i) & "</a></p>")
		next
	end function
%>

<%
	' 获取文件最后修改的时间
	function getLastModified(filePath)
		dim fileObj
		dim temp
		set fileObj = Server.CreateObject("Scripting.FileSystemObject")
		set temp = fileObj.GetFile(Server.MapPath(filePath))
		getLastModified = temp.DateLastModified
	end function
%>

<%
	' 读取xml文件
	function readXML(fileName)
		dim xmlDoc
		set xmlDoc = Server.CreateObject("Microsoft.XMLDOM")
		xmlDoc.async = false
		xmlDoc.load(Server.MapPath("data/" & filename))
		set readXML = xmlDoc
	end function
%>

<%
	' 初始化课程
	function initCourses()
		dim xml
		set xml = readXML("courses.xml")
		dim parts
		set parts = xml.getElementsByTagName("course")
		dim i,j
		dim part
		if parts.length > 0 then
			dim courses()
			redim courses(parts.length - 1)
			dim course(2)
			
			for i = 0 to parts.length - 1
				set part = parts.item(i)
				for j = 0 to 2
					course(j) = part.getElementsByTagName(Application("course_fields")(j)).item(0).text
				next
				courses(i) = course
			next
			
			Application("courses") = courses
		end if

	end function
%>
<%
	' 初始化课程表
	function initTimetable()
		dim xml
		set xml = readXML("timetable.xml")
		dim parts
		set parts = xml.getElementsByTagName("schedule")
		dim i,j
		dim part
		if parts.length > 0 then
			dim timetable()
			redim timetable(parts.length - 1)
			dim schedule(6)
			dim timetableIndex(6,5)
			dim unscheduled()
			redim unscheduled(ubound(Application("courses")))
			dim unscheduledIndex()
			
			for i = 0 to parts.length - 1
				set part = parts.item(i)
				for j = 0 to 4
					schedule(j) = part.getElementsByTagName(Application("timetable_fields")(j)).item(0).text
				next
				
				' 根据课程补全课程表信息
				for j =0 to ubound(Application("courses"))
					if schedule(2) = Application("courses")(j)(1) then
						schedule(5) = Application("courses")(j)(0)
						schedule(6) = Application("courses")(j)(2)
						unscheduled(j) = false
						exit for
					end if
				next
				
				timetableIndex(int(schedule(0))-1,int(schedule(1))-1) = _
					timetableIndex(int(schedule(0))-1,int(schedule(1))-1) & i & ","
				
				timetable(i) = schedule
			next
			
			j = 0
			' 未安排的课程
			for i = 0 to ubound(unscheduled)
				if isempty(unscheduled(i)) then
					redim preserve unscheduledIndex(j)
					unscheduledIndex(j) = i
					j = j+1
				end if
			next
			
			if j > 0 then
				Application("unscheduled") = unscheduledIndex
			end if
			
			Application("timetable") = timetable
			Application("timetable_index") = timetableIndex
		end if
	end function
%>

<%
	' 根据选择创建课程安排表格
	function createTimetableTable()
		if isempty(Application("timetable")) then
			exit function
		end if
		if not isempty(Application(Application("choices")(Session("choice_index")))) then
			Response.Write(Application(Application("choices")(Session("choice_index"))))
			exit function
		end if
		
		dim statement
		statement = ""
		dim timetable
		dim i,j,k
		
		if Application("choices")(Session("choice_index")) = "全部" and _
				not isempty(Application("timetable")) then
			statement = "<table align='center'>"
			statement = statement & "<tr><th class='small'></th>"
			for i = 0 to 6
				statement = statement & "<th class='small'>" & Application("choices")(i) & "</th>"
			next
			statement = statement & "</tr>"
			
			for i = 0 to 5
				statement = statement & "<tr><th  class='small'>" & Application("sections")(i) & "</th>"
				for j = 0 to 6
					statement = statement & "<td class='small'>"
					timetable = getTimetable(j,i)
					if not isempty(timetable) then
						for k = 0 to ubound(timetable)
							statement = statement & timetable2string(timetable(k))
							if k < ubound(timetable) then
								statement = statement & "<br><font color='yellow'>* * *</font><br>"
							end if
						next
					end if
					statement = statement & "</td>"
				next
				statement = statement & "</tr>"
			next
			
			if not isempty(Application("unscheduled")) then ' 存在未安排的课程
				statement = statement & "<tr><td class='small' colspan='8' align='left'>未安排的课程："
				for i = 0 to ubound(Application("unscheduled"))
					j = Application("unscheduled")(i)
					statement = statement & Application("courses")(j)(0) & " " & _
						Application("courses")(j)(1) & " " & _
						Application("courses")(j)(2)
					if i<ubound(Application("unscheduled")) then
						statement = statement & " , "
					else
						statement = statement & " ."
					end if
				next
				statement = statement & "</td></tr>"
			end if
			
			statement = statement & "</table>"
		else
			for i = 0 to 5
				timetable = getTimetable(Session("choice_index"),i)
				if not isempty(timetable) then
					if statement="" then
						statement = "<table align='center'>"
						statement = statement & "<tr><th>节次</th>" & _
							"<th>课程号</th>" & _
							"<th >课程</th>" & _
							"<th >老师</th>" & _
							"<th >周次</th></tr>"
					end if
					for j = 0 to ubound(timetable)
						statement = statement & "<tr>"
						if j =0 then
							statement = statement & "<td rowspan='" & (ubound(timetable)+1) & "'>" & _
								Application("sections")(i) & "</td>"
						end if
						statement = statement &  "<td >" & timetable(j)(5) & "</td>" & _
							"<td >" & timetable(j)(2) & "</td>" & _
							"<td >" & timetable(j)(6) & "</td>" & _
							"<td >" & timetable(j)(3) & "</td></tr>"
					next
				end if
			next
			if not statement="" then
				statement = statement & "</table>"
			end if
		end if
		
		Application(Application("choices")(Session("choice_index"))) = statement
		Response.Write(statement)
	end function
%>

<%
	' 获取指定日期和节次的课程
	function getTimetable(d,section)
		if not isempty(Application("timetable_index")(d,section)) then
			dim timetable()
			dim timetableIndex
			dim i
			timetableIndex = split(Application("timetable_index")(d,section),",")
			for i = 0 to ubound(timetableIndex)
				
				' 非空串
				if timetableIndex(i)<>"" then
					Redim Preserve timetable(i)
					timetable(i) = Application("timetable")(int(timetableIndex(i)))
				end if
				
			next
			getTimetable = timetable
		else
			getTimetable = empty
		end if
	end function
%>

<%
	' 课程安排转字符串
	function timetable2string(timetable)
		timetable2string = timetable(5) & "<br>" & _
			timetable(2) & "<br>" & _
			timetable(6) & "<br>" & _
			timetable(3) & "<br>" & _
			timetable(4)
	end function
%>

<%
	' 创建今天课程安排区
	function createTodayZone()
		if Application("date") = DateValue(Session("time")) then
			Response.Write(Application("today"))
			exit function
		end if
		
		dim w
		w = int(datediff("s",Application("term_start"),Session("time"))/(7*24*60*60) +1) ' 周次
		Session("week") = w
		
		dim d,dIndex
		if weekday(Session("time")) = 1 then
			dIndex  = 6
		else
			dIndex  = weekday(Session("time")) -2
		end if
		d = Application("choices")(dIndex) ' 星期
		
		dim t
		t = year(Session("time")) & "年" & month(Session("time")) & "月" & day(Session("time")) & "日 " & d &_
			"<br>第" & w & "周" ' 日期
		
		dim statement
		statement = ""
		
		if (not isempty(Application("timetable"))) and w >= 0 and w <= Application("max_week") then ' 今天在学期范围内
			dim temp
			dim timetable()
			dim i,j,k
			
			for i = 0 to 5
				temp = getTimetable(dIndex,i) ' 获取今天指定节次可能的课程安排
				if not isempty(temp) then ' 今天存在可能的课程安排
					k = 0
					for j =0 to ubound(temp)
						if shouldAttend(temp(j)) then ' 确定是今天的课程安排
							redim preserve timetable(k)
							timetable(k) = temp(j)
							k = k + 1
						end if
					next
					
					if k>0 then ' 确定今天有课程安排，生成今日课程安排表格
						if statement="" then
							statement = statement & "<table id='table_today' align='center'>"
						end if
						statement = statement & "<tr><th class='small'>"& Application("sections")(i) &"</th><td class='small'>"
						for j = 0 to ubound(timetable)
							statement = statement & timetable2string(timetable(j))
							if j < ubound(timetable) then
								statement = statement & "<br><font color='yellow'>* * *</font><br>"
							end if
						next
						statement = statement & "</td></tr>"
					end if
				end if
			next
			
			if not statement="" then
				statement = statement & "</table>"
			end if
		end if
		
		statement = t & statement
		
		Application("date") = DateValue(Session("time"))
		Application("today") = statement
		
		Response.Write(statement)
	end function
%>

<%
	' 根据课程安排的周次判断是否今天的课程安排
	function shouldAttend(timetable)
		shouldAttend = false
		
		dim w
		w = timetable(3) ' 课程安排的周次
		w = replace(w," ","")
		
		if instr(w,"单")>0 then ' 单周课程
			if Session("week") mod 2=0 then ' 双周不需要上课
				exit function
			end if
			w = mid(1,instr(w,"单")-1)
		elseif instr(w,"双")>0 then ' 双周课程
			if Session("week") mod 2=1 then ' 单周不需要上课
				exit function
			end if
			w = mid(1,instr(w,"双")-1)
		end if
		
		dim temp
		temp = split(w,"-")
		
		dim weeks()
		dim i,j
		j = 0
		
		' 获取课程安排周次范围
		for i = 0 to ubound(temp)
			if not temp(i) = "" then
				redim preserve weeks(j)
				weeks(j) = int(temp(i))
				j = j + 1
			end if
		next
		
		if j = 1 then
			if not weeks(0) = Session("week") then
				exit function
			end if
		elseif j > 1 then
			if not (Session("week") >= weeks(0) and Session("week") <= weeks(1)) then
				exit function
			end if
		end if
		
		shouldAttend = true
	end function
%>