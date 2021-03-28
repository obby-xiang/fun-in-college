const DAY = ["星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"]; // 日期

const TABLE_HEADER = ["节次", "课程号", "课程", "老师", "周次", "地点"]; // 单日课程表表头信息

const TABLE_HEADER_COMPLETE = ["", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期日"]; // 全部课程表表头信息

const SECTION = ["第一大节", "第二大节", "第三大节", "第四大节", "第五大节", "第六大节"]; // 节次

/* 背景颜色，根据日期选择显示不同的背景颜色 */
const COLOR = ["#ba55d3",
    "#00fa9a",
    "#48d1cc",
    "#66cdaa",
    "#3cb371",
    "#9370db",
    "#7b68ee",
    "#c71585"];

const COURSE_DATA_PATH = "data/courses.xml"; // 课程数据路径
const TIMETABLE_DATA_PATH = "data/timetable.xml"; // 课程安排数据路径

const START_DATE = new Date("Mar 05 2018 GMT"); // 学期起始日期

let courses; // 课程
let timetable; // 课程安排
let day; // 选择的日期
let table; // 表格
let unscheduled; // 未安排的课程
let isToday; // 是否今日课程网页调用
let date; // 今天日期
let week; // 周次

/**
 * 入口，供网页课程表网页和今日课程网页调用
 *
 * @param tag 标志，"timetable":课程表网页调用，"today":今日课程网页调用
 * @param d 选择的日期，tag为"timetable"时有效，未定义时课程表网页显示全部课程表，否则显示指定日期的课程表
 */
function run(tag, d) {
    switch (tag) {
        case "timetable":
            day = d;
            isToday = false;
            break;
        case "today":
            isToday = true;
    }
    loadData(COURSE_DATA_PATH); // 请求并加载课程数据
}


/**
 * 请求并加载指定文件的数据
 *
 * @param fileName 需要加载的文件的文件名
 */
function loadData(fileName) {
    let httpRequest;

    if (window.XMLHttpRequest) { // IE7+, Firefox, Chrome, Opera, Safari
        httpRequest = new XMLHttpRequest();
    }
    else { // IE6, IE5
        httpRequest = new ActiveXObject("Microsoft.XMLHTTP");
    }

    /* 响应状态改变事件 */
    httpRequest.onreadystatechange = function () {
        if (httpRequest.readyState === 4 && httpRequest.status === 200) { // 请求已完成，且响应已就绪；状态为OK

            if (fileName === COURSE_DATA_PATH) { // 获得课程文件数据
                if (!isToday) {
                    changeBgColor();
                }
                initCourses(httpRequest.responseXML); // 根据课程数据初始化课程
                loadData(TIMETABLE_DATA_PATH); // 请求并加载课程安排数据

            } else if (fileName === TIMETABLE_DATA_PATH) { // 获得课程安排文件数据
                initTimeTable(httpRequest.responseXML); // 根据课程安排数据初始化课程安排

                if (isToday) { // 今日课程网页调用

                    updateTime(); // 获取服务器时间并生成今日课程表格

                    /* 定时操作，每一分钟获取服务器时间并生成今日课程表格 */
                    setInterval(function () {
                        updateTime();
                    }, 60 * 1000);

                }
                else { // 课程表网页调用

                    createTable(); // 创建显示课程表的表格

                }

            }

        }
    };
    httpRequest.open("GET", fileName, true); // 异步
    httpRequest.send();
}


/**
 * 根据已读取的课程数据初始化课程表
 *
 * @param data 课程数据
 */
function initCourses(data) {

    let dataParts = data.documentElement.getElementsByTagName("course");
    courses = [];
    let part;
    let id;
    let course;
    let teacher;
    for (let i = 0, j = dataParts.length; i < j; i++) {
        part = dataParts[i].getElementsByTagName("id"); // 课程号
        try {
            id = part[0].firstChild.nodeValue;
        } catch (e) {
            id = "";
        }
        part = dataParts[i].getElementsByTagName("name"); // 课程名
        try {
            course = part[0].firstChild.nodeValue;
        } catch (e) {
            course = "";
        }
        part = dataParts[i].getElementsByTagName("teacher"); // 老师
        try {
            teacher = part[0].firstChild.nodeValue;
        } catch (e) {
            teacher = "";
        }
        courses[i] = {id: id, course: course, teacher: teacher};
    }
}

/**
 * 根据已读取的课程安排数据初始化课程表
 *
 * @param data 课程安排数据
 */
function initTimeTable(data) {
    let dataParts = data.documentElement.getElementsByTagName("schedule");
    timetable = [];
    unscheduled = [];
    let part;
    let day;
    let section;
    let course;
    let week;
    let place;
    let scheduled = [];
    for (let i = 0, j = dataParts.length; i < j; i++) {
        part = dataParts[i].getElementsByTagName("day"); // 日期
        try {
            day = part[0].firstChild.nodeValue;
        } catch (e) {
            day = "";
        }
        part = dataParts[i].getElementsByTagName("section"); // 节次
        try {
            section = part[0].firstChild.nodeValue - 1;
        } catch (e) {
            section = "";
        }
        part = dataParts[i].getElementsByTagName("course"); // 课程
        try {
            course = part[0].firstChild.nodeValue;
        } catch (e) {
            course = "";
        }
        part = dataParts[i].getElementsByTagName("week"); // 周次
        try {
            week = part[0].firstChild.nodeValue;
        } catch (e) {
            week = "";
        }
        part = dataParts[i].getElementsByTagName("place"); // 地点
        try {
            place = part[0].firstChild.nodeValue;
        } catch (e) {
            place = "";
        }
        timetable[i] = {day: day, section: section, course: getCourseIndex(course), week: week, place: place};

        scheduled[i] = timetable[i].course;
    }

    /* 排序课程数据，按日期、节次排序 */
    timetable.sort(function (a, b) {
        if (a.day === b.day) {
            if (a.section === b.section) {
                return 0;
            }
            else if (a.section > b.section) {
                return 1;
            }
            else {
                return -1;
            }
        }
        else if (a.day > b.day) {
            return 1;
        } else {
            return -1;
        }
    });

    /* 找出未安排的课程 */
    for (let i = 0, j = courses.length, k = 0; i < j; i++) {
        if (scheduled.indexOf(i) === -1) {
            unscheduled[k++] = i;
        }
    }
}

/**
 * 获取服务器时间并生成今日课程表格
 */
function updateTime() {
    let httpRequest;

    if (window.XMLHttpRequest) { // IE7+, Firefox, Chrome, Opera, Safari
        httpRequest = new XMLHttpRequest();
    }
    else { // IE6, IE5
        httpRequest = new ActiveXObject("Microsoft");
    }

    httpRequest.onreadystatechange = function () {

        if (httpRequest.readyState === 4) { // 请求已完成

            let temp = new Date(httpRequest.getResponseHeader("date")); // 获取的服务器时间
            temp.setTime(temp.getTime() + 8 * 60 * 60 * 1000); // 虽然获取的时间是准确的，但是为了后面方便，还是加上8小时

            if (date === undefined || date.getUTCDate() !== temp.getUTCDate()) { // 日期未定义或日期的天改变，更新表格

                date = temp;
                day = DAY[date.getUTCDay()]; // 星期
                week = Math.floor((date.getTime() - START_DATE.getTime()) / (7 * 24 * 60 * 60 * 1000)) + 1; // 周次

                /* 显示当前日期 */
                document.getElementById("date").innerHTML = date.getUTCFullYear() + "年"
                    + (date.getUTCMonth() + 1) + "月"
                    + date.getUTCDate() + "日 "
                    + day + "<br>"
                    + "第" + week + "周";

                createTodayTimetable(); // 创建今日课程表格
            }
        }

    };

    httpRequest.open("GET", undefined, true); // 异步
    httpRequest.send();
}


/**
 * 更改背景颜色
 */
function changeBgColor() {
    let color = COLOR[DAY.length]; // 显示全部课程表时背景颜色
    if (day !== undefined) { // 根据选择的日期指定背景颜色
        for (let i = 0, j = DAY.length; i < j; i++) {
            if (day === DAY[i]) {
                color = COLOR[i];
                break;
            }
        }
    }
    let frames = parent.window.frames; // 获取窗口的所有框架

    /* 设置框架背景颜色 */
    for (let i = 0, j = frames.length; i < j; i++) {
        frames[i].document.bgColor = color;
    }
}

/**
 * 获取课程的索引
 *
 * @param course 课程
 * @returns {number} 课程的索引
 */
function getCourseIndex(course) {
    for (let i = 0, j = courses.length; i < j; i++) {
        if (courses[i].course === course) {
            return i;
        }
    }
    return -1;
}

/**
 * 创建表格，放置在课程表网页
 */
function createTable() {

    if (day !== undefined && getTimetableFromDay(day).length === 0) { // 选择了日期但是没有课程
        return;
    }

    table = document.createElement("table"); // 创建表格
    table.className = "timetable";

    createTableHeader(); // 创建表头

    let result = [];

    if (day === undefined) { // 未指定日期，显示所有课程表

        for (let i = 0, j = SECTION.length; i < j; i++) { // 对每一节次

            let temp = getTimetableFromSection(i); // 获取该节次所有日期的课程

            result = [];

            /* 将课程安排转化为字符串，同时合并同日期同节次的课程在一个字符串 */
            for (let p = 0, q = temp.length, k = 0; p < q; p++) {
                if (p === 0 || temp[p].day !== temp[p - 1].day) {
                    result[k++] = timetable2String(temp[p]); // 课程安排字符串
                } else {
                    result[k - 1] = {
                        text: result[k - 1].text + "<br>******<br>" + timetable2String(temp[p]).text,
                        position: result[k - 1].position
                    };
                }
            }

            createTableRow(result, i); // 创建行
        }

        if (unscheduled.length > 0) { // 存在未安排的课程，在课程表新增一行用于显示未安排的课程的信息
            let text = "未安排的课程: ";
            for (let i = 0, j = unscheduled.length; i < j; i++) {
                text += courses[unscheduled[i]].id + " " + courses[unscheduled[i]].course + " " + courses[unscheduled[i]].teacher;
                if (i < j - 1) {
                    text += "; "
                }
                else {
                    text += "."
                }
            }
            let row = table.insertRow(table.rows.length);
            let cell = row.insertCell();
            cell.className = "contentM";
            cell.style.textAlign = "left";
            cell.colSpan = DAY.length + 1;
            cell.innerHTML = text;

        }
    } else {

        result = getTimetableFromDay(day); // 获取指定日期的课程

        if (result.length === 0) { // 指定的日期没有课程
            return;
        }

        for (let i = 0, j = result.length; i < j; i++) {
            createTableRow(result[i]); // 创建行
        }

    }

    document.getElementById("timetable").appendChild(table); // 添加表格到指定分区
}


/**
 * 创建表头
 */
function createTableHeader() {

    let row = table.insertRow(); // 插入行
    let header = day === undefined ? TABLE_HEADER_COMPLETE : TABLE_HEADER; // 选择表头显示的信息
    let cell;

    /* 添加单元格 */
    for (let i = 0, j = header.length; i < j; i++) {
        cell = row.insertCell();
        cell.className = day === undefined ? "headerM" : "headerL";
        cell.innerHTML = header[i];
    }
}


/**
 * 创建表格行
 *
 * @param result 行显示信息
 * @param section 节次，当节次未定义时，创建的是指定日期的表格，否则创建的是全部课程表的表格
 */
function createTableRow(result, section) {
    let row = table.insertRow(table.rows.length); // 在末尾处添加行
    let cell;
    if (section === undefined) { // 未定义节次
        let text = [
            SECTION[result.section],
            courses[result.course].id,
            courses[result.course].course,
            courses[result.course].teacher,
            result.week,
            result.place
        ];
        for (let i = 0, j = text.length; i < j; i++) {
            cell = row.insertCell(row.cells.length);
            cell.className = "contentL";
            cell.innerHTML = text[i];
        }

    } else { // 定义了节次
        cell = row.insertCell();
        cell.className = "headerM";
        cell.innerHTML = SECTION[section];
        for (let i = 1, j = DAY.length, k = 0, n = result.length; i <= j; i++) {
            cell = row.insertCell(row.cells.length);
            cell.className = "contentM";
            if (k < n && result[k].position + 1 === i) {
                cell.innerHTML = result[k].text;
                k++;
            }
        }
    }
}

/**
 * 获取指定日期的课程
 *
 * @param day 指定的日期
 * @returns {Array} 指定日期的课程
 */
function getTimetableFromDay(day) {
    let result = [];
    for (let i = 0, j = timetable.length, k = 0; i < j; i++) {
        if (day === DAY[timetable[i].day]) {
            result[k++] = timetable[i];
        }
    }
    return result;
}

/**
 * 获取指定节次的所有日期的所有课程
 *
 * @param section 指定的节次
 * @returns {Array} 指定的节次的所有日期的所有课程
 */
function getTimetableFromSection(section) {
    let result = [];
    for (let i = 0, j = timetable.length, k = 0; i < j; i++) {
        if (section === timetable[i].section) {
            result[k++] = timetable[i];
        }
    }
    return result;
}

/**
 * 创建今日课程表格，放置在今日课程网页
 */
function createTodayTimetable() {

    document.getElementById("today").innerHTML = ""; // 清空分区

    let timetable = getTimetableFromDay(day); // 获取今日课程

    if (timetable.length === 0) { // 今天没有课程
        return;
    }

    let result = [];

    /* 将课程安排转化为字符串，同时合并同一节次的课程在一个字符串 */
    for (let i = 0, j = timetable.length, k = -1, m = 0; i < j; i++) {
        if (!shouldAttendClass(timetable[i].week)) { // 课程周次是否含有本周
            continue;
        }
        if (timetable[i].section !== k) {
            k = timetable[i].section;
            result[m++] = {section: timetable[i].section, text: timetable2String(timetable[i]).text};
        } else {
            result[m - 1] = {
                section: result[m - 1].section,
                text: result[m - 1].text + "<br>******<br>" + timetable2String(timetable[i]).text
            };
        }
    }

    if (result.length === 0) { // 今天没有课程
        return;
    }

    table = document.createElement("table"); // 创建表格
    table.className = "today";

    let row;
    let cell;
    for (let i = 0, j = result.length; i < j; i++) {
        row = table.insertRow(table.rows.length);
        cell = row.insertCell();
        cell.className = "headerM";
        cell.innerHTML = SECTION[result[i].section];
        cell = row.insertCell(row.cells.length);
        cell.className = "contentM";
        cell.innerHTML = result[i].text;
    }

    document.getElementById("today").appendChild(table); // 添加表格到指定分区
}

/**
 * 课程周次是否含有本周
 *
 * @param condition 课程周次
 * @returns {boolean} 含有本周返回true，否则返回false
 */
function shouldAttendClass(condition) {
    condition = condition.replace(new RegExp(" ", "g"), ""); // 去除所有空格

    if (condition.indexOf("单") !== -1) { // 单周
        if (week % 2 === 0) {
            return false;
        }
        condition = condition.replace(new RegExp("单", "g"), "");
    }
    else if (condition.indexOf("双") !== -1) { // 双周
        if (week % 2 === 1) {
            return false;
        }
        condition = condition.replace(new RegExp("双", "g"), "");
    }

    if (condition === "") { // 课程周次信息不足
        return false;
    }

    let numbers = condition.split("-");
    if (numbers.length === 0) {
        return false;
    }
    let start = parseInt(numbers[0]), end = parseInt(numbers[numbers.length - 1]); // 课程开始周和结束周
    return week >= start && week <= end;
}


/**
 * 课程安排转化为字符串（其实还包含全部课程表显示的位置信息）
 *
 * @param timetable 课程安排
 * @returns {{text: string, position: number}} 字符串（以及用于全部课程表显示的位置信息）
 */
function timetable2String(timetable) {
    return {
        text: courses[timetable.course].id + "<br>"
        + courses[timetable.course].course + "<br>"
        + courses[timetable.course].teacher + "<br>"
        + timetable.week + "<br>"
        + timetable.place,
        position: (parseInt(timetable.day) === 0 ? DAY.length : timetable.day) - 1
    };
}

/**
 * 响应点击链接事件
 *
 * @param id 链接的id
 */
function onLinkClick(id) {

    let links = document.getElementsByTagName("a"); // 获取所有链接

    /* 设置链接样式 */
    for (let i = 0, j = links.length; i < j; i++) {
        if (id === links[i].id) {
            links[i].style.fontSize = "26px";
            links[i].style.color = "white";
        }
        else {
            links[i].style.fontSize = "22px";
            links[i].style.color = "darkblue";
        }
    }
}
