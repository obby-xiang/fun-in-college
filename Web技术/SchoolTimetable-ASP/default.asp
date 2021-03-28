<%@LANGUAGE="VBSCRIPT" CODEPAGE="65001"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <link href="css/style.css" rel="stylesheet" type="text/css" />
    <!-- #include file ="vb/functions.asp" -->
	<%
		Application.Lock() ' 锁定
		create()
	%>
	<title>刘翔课程表</title>
</head>
<body bgcolor="<% Response.Write(Session("background_color")) %>">
	
	<table class="borderless" width="100%" height="100%">
    	<tr>
        	<td class="borderless" width="80%" height="100%">
            	<table class="borderless" width="100%" height="100%" >
                	<tr>
                    	<td id="td_title" class="borderless" width="100%" height="15%" colspan="2">刘翔课程表</td>
                    </tr>
                    <tr>
                    	<td id="td_choices" class="borderless" width="20%" height="85%">
                            <%
								createChoiceLinks()
							%>
                        </td>
                        <td id="td_timetable" class="borderless" width="80%" height="85%" >
                        	<% 
								createTimetableTable()
							%>
                        </td>
                    </tr>
                </table>
            </td>
            <td  class="borderless" width="80%" height="100%">
            	<div id="div_today_zone">
            		<div id="div_today">
                		<%
							createTodayZone()
						%>
                	</div>
                    <div id="div_count">访问量：<% Response.Write(Application("count")) %></div>
                </div>
            </td>
        </tr>
    </table>
    <%
		Application.UnLock() ' 开锁
	%>
</body>
</html>
