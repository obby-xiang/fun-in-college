% @since 2017-12-26
function main
%% 程序入口

% 添加路径
addpath('./functions');

global handles;
try
    if isstruct(handles) && ishandle(handles.figure_main)    % 窗口已存在
        return;
    end
catch
end
handles = struct;


% 窗口
handles.figure_main = figure('tag','figure_main',...
    'name','DIP201500800605',...    % 窗口标题
    'numbertitle','off',...    % 关闭数字标题
    'menubar','none',...    % 去除菜单栏
    'toolbar','figure',...    % 显示工具栏
    'resize','on',...    % 窗口大小可变
    'dockcontrols','off',...    % 取消可停靠
    'windowstyle','normal',...    % 默认窗口风格
    'closerequestfcn',@(hObject,eventdata)exit_Callback(hObject,eventdata),...    % 窗口将要关闭时回调
    'units','normalized',...
    'position',[0.15 0.1 0.7 0.7]);    % 窗口位置及大小


% 选项卡组
handles.uitabgroup_main = uitabgroup(handles.figure_main,'tag','uitabgroup_main',...
    'units','normalized',...
    'selectionchangedfcn',@(hObject,eventdata)uimenu_edit_Callback(hObject,eventdata),...    % 调用编辑菜单函数
    'position',[0 0 1.0 1.0]);


handles.uimenu_file = uimenu(handles.figure_main,'tag','uimenu_file',...
    'label','文件',...
    'callback',@(hObject,eventdata)file_Callback(hObject,eventdata));
handles.uimenu_file_open = uimenu(handles.uimenu_file,'tag','uimenu_file_open',...
    'label','打开...',...
    'accelerator','O',...
    'callback',@(hObject,eventdata)file_open_Callback(hObject,eventdata));
handles.uimenu_file_close = uimenu(handles.uimenu_file,'tag','uimenu_file_close',...
    'label','关闭',...
    'accelerator','W',...
    'callback',@(hObject,eventdata)uitab_close_Callback(hObject,eventdata));    % 调用选项卡关闭函数
handles.uimenu_file_save = uimenu(handles.uimenu_file,'tag','uimenu_file_save',...
    'label','保存',...
    'separator','on',...
    'accelerator','S',...
    'callback',@(hObject,eventdata)file_save_Callback(hObject,eventdata));
handles.uimenu_file_saveas = uimenu(handles.uimenu_file,'tag','uimenu_file_saveas',...
    'label','另存为...',...
    'callback',@(hObject,eventdata)file_saveas_Callback(hObject,eventdata));
handles.uimenu_file_exit = uimenu(handles.uimenu_file,'tag','uimenu_file_exit',...
    'label','退出',...
    'separator','on',...
    'accelerator','Q',...
    'callback',@(hObject,eventdata)exit_Callback(hObject,eventdata));


handles.uimenu_edit = uimenu(handles.figure_main,'tag','uimenu_edit',...
    'label','编辑',...
    'callback',@(hObject,eventdata)uimenu_edit_Callback(hObject,eventdata));
handles.uimenu_edit_undo = uimenu(handles.uimenu_edit,'tag','uimenu_edit_undo',...
    'label','撤销',...
    'accelerator','Z',...
    'callback',@(hObject,eventdata)undo_Callback(hObject,eventdata));
handles.uimenu_edit_redo = uimenu(handles.uimenu_edit,'tag','uimenu_edit_redo',...
    'label','恢复',...
    'accelerator','Y',...
    'callback',@(hObject,eventdata)redo_Callback(hObject,eventdata));
handles.uimenu_edit_reset = uimenu(handles.uimenu_edit,'tag','uimenu_edit_reset',...
    'label','重置',...
    'separator','on',...
    'accelerator','R',...
    'callback',@(hObject,eventdata)reset_Callback(hObject,eventdata));


handles.uimenu_basicOp = uimenu(handles.figure_main,'tag','uimenu_basicOp',...
    'label','基本操作',...
    'callback',@(hObject,eventdata)uimenu_basicOp_Callback(hObject,eventdata));
handles.uimenu_gray = uimenu(handles.uimenu_basicOp,'tag','uimenu_gray',...
    'label','灰度化',...
    'callback',@(hObject,eventdata)uimenu_gray_Callback(hObject,eventdata));
handles.uimenu_binary = uimenu(handles.uimenu_basicOp,'tag','uimenu_binary',...
    'label','二值化...',...
    'callback',@(hObject,eventdata)uimenu_binary_Callback(hObject,eventdata));
handles.uimenu_rgb = uimenu(handles.uimenu_basicOp,'tag','uimenu_rgb',...
    'label','彩色化...',...
    'callback',@(hObject,eventdata)uimenu_rgb_Callback(hObject,eventdata));


handles.uimenu_graytransf = uimenu(handles.figure_main,'tag','uimenu_graytransf',...
    'label','灰度变换',...
    'callback',@(hObject,eventdata)uimenu_graytransf_Callback(hObject,eventdata));
handles.uimenu_imreverse = uimenu(handles.uimenu_graytransf,'tag','uimenu_imreverse',...
    'label','反转变换',...
    'callback',@(hObject,eventdata)uimenu_imreverse_Callback(hObject,eventdata));
handles.uimenu_imlogtransf = uimenu(handles.uimenu_graytransf,'tag','uimenu_imlogtransf',...
    'label','对数变换',...
    'callback',@(hObject,eventdata)uimenu_imlogtransf_Callback(hObject,eventdata));
handles.uimenu_imgammatransf = uimenu(handles.uimenu_graytransf,'tag','uimenu_imgammatransf',...
    'label','幂律变换...',...
    'callback',@(hObject,eventdata)uimenu_imgammatransf_Callback(hObject,eventdata));
handles.uimenu_imbitplane = uimenu(handles.uimenu_graytransf,'tag','uimenu_imbitplane',...
    'label','比特平面分层...',...
    'callback',@(hObject,eventdata)uimenu_imbitplane_Callback(hObject,eventdata));


handles.uimenu_histprocess = uimenu(handles.figure_main,'tag','uimenu_histprocess',...
    'label','直方图处理',...
    'callback',@(hObject,eventdata)uimenu_histprocess_Callback(hObject,eventdata));
handles.uimenu_histeq = uimenu(handles.uimenu_histprocess,'tag','uimenu_histeq',...
    'label','直方图均衡');
handles.uimenu_histeqwhole = uimenu(handles.uimenu_histeq,'tag','uimenu_histeqwhole',...
    'label','全局',...
    'callback',@(hObject,eventdata)uimenu_histeqwhole_Callback(hObject,eventdata));
handles.uimenu_histeqlocal = uimenu(handles.uimenu_histeq,'tag','uimenu_histeqlocal',...
    'label','局部...',...
    'callback',@(hObject,eventdata)uimenu_histeqlocal_Callback(hObject,eventdata));
handles.uimenu_histloceh = uimenu(handles.uimenu_histprocess,'tag','uimenu_histloceh',...
    'label','局部增强...',...
    'callback',@(hObject,eventdata)uimenu_histloceh_Callback(hObject,eventdata));


handles.uimenu_affinetransf = uimenu(handles.figure_main,'tag','uimenu_affinetransf',...
    'label','仿射变换',...
    'callback',@(hObject,eventdata)uimenu_affinetransf_Callback(hObject,eventdata));
handles.uimenu_imresize = uimenu(handles.uimenu_affinetransf,'tag','uimenu_imresize',...
    'label','缩放...',...
    'callback',@(hObject,eventdata)uimenu_imresize_Callback(hObject,eventdata));
handles.uimenu_imrotate = uimenu(handles.uimenu_affinetransf,'tag','uimenu_uimenu_imrotate',...
    'label','旋转...',...
    'callback',@(hObject,eventdata)uimenu_imrotate_Callback(hObject,eventdata));
handles.uimenu_imsheartransf = uimenu(handles.uimenu_affinetransf,'tag','uimenu_imsheartransf',...
    'label','剪切...',...
    'callback',@(hObject,eventdata)uimenu_imsheartransf_Callback(hObject,eventdata));


handles.uimenu_spafilter = uimenu(handles.figure_main,'tag','uimenu_spafilter',...
    'label','空间滤波',...
    'callback',@(hObject,eventdata)uimenu_spafilter_Callback(hObject,eventdata));
handles.uimenu_fuzspafilter = uimenu(handles.uimenu_spafilter,'tag','uimenu_fuzspafilter',...
    'label','平滑');
handles.uimenu_fuzspafilter_mean = uimenu(handles.uimenu_fuzspafilter,'tag','uimenu_fuzspafilter_mean',...
    'label','均值...',...
    'callback',@(hObject,eventdata)uimenu_fuzspafilter_mean_Callback(hObject,eventdata));
handles.uimenu_fuzspafilter_gaussian = uimenu(handles.uimenu_fuzspafilter,'tag','uimenu_fuzspafilter_gaussian',...
    'label','高斯...',...
    'callback',@(hObject,eventdata)uimenu_fuzspafilter_gaussian_Callback(hObject,eventdata));
handles.uimenu_sharpspafilter = uimenu(handles.uimenu_spafilter,'tag','uimenu_sharpspafilter',...
    'label','锐化');
handles.uimenu_sharpspafilter_firstder = uimenu(handles.uimenu_sharpspafilter,'tag','uimenu_sharpspafilter_firstder',...
    'label','一阶微分',...
    'callback',@(hObject,eventdata)uimenu_sharpspafilter_firstder_Callback(hObject,eventdata));
handles.uimenu_sharpspafilter_secondder = uimenu(handles.uimenu_sharpspafilter,'tag','uimenu_sharpspafilter_secondder',...
    'label','二阶微分',...
    'callback',@(hObject,eventdata)uimenu_sharpspafilter_secondder_Callback(hObject,eventdata));
handles.uimenu_spafilter_others = uimenu(handles.uimenu_spafilter,'tag','uimenu_spafilter_others',...
    'label','其他');
handles.uimenu_iminvharmeanfilter = uimenu(handles.uimenu_spafilter_others,'tag','uimenu_iminvharmeanfilter',...
    'label','逆谐波均值...',...
    'callback',@(hObject,eventdata)uimenu_iminvharmeanfilter_Callback(hObject,eventdata));
handles.uimenu_immodalpmeanfilter = uimenu(handles.uimenu_spafilter_others,'tag','uimenu_immodalpmeanfilter',...
    'label','修正的阿尔法均值...',...
    'callback',@(hObject,eventdata)uimenu_immodalpmeanfilter_Callback(hObject,eventdata));
handles.uimenu_imadprednoisfilter = uimenu(handles.uimenu_spafilter_others,'tag','uimenu_imadprednoisfilter',...
    'label','自适应局部降低噪声...',...
    'callback',@(hObject,eventdata)uimenu_imadprednoisfilter_Callback(hObject,eventdata));
handles.uimenu_imadpmedianfilter = uimenu(handles.uimenu_spafilter_others,'tag','uimenu_imadpmedianfilter',...
    'label','自适应中值...',...
    'callback',@(hObject,eventdata)uimenu_imadpmedianfilter_Callback(hObject,eventdata));


handles.uimenu_freqfilter = uimenu(handles.figure_main,'tag','uimenu_freqfilter',...
    'label','频域滤波',...
    'callback',@(hObject,eventdata)uimenu_freqfilter_Callback(hObject,eventdata));
handles.uimenu_lowpassfreqfilter = uimenu(handles.uimenu_freqfilter,'tag','uimenu_lowpassfreqfilter',...
    'label','低通');
handles.uimenu_idealLPfreqfilter = uimenu(handles.uimenu_lowpassfreqfilter,'tag','uimenu_idealLPfreqfilter',...
    'label','理想低通...',...
    'callback',@(hObject,eventdata)uimenu_idealLPfreqfilter_Callback(hObject,eventdata));
handles.uimenu_butterworthLPfreqfilter = uimenu(handles.uimenu_lowpassfreqfilter,'tag','uimenu_butterworthLPfreqfilter',...
    'label','布特沃斯低通...',...
    'callback',@(hObject,eventdata)uimenu_butterworthLPfreqfilter_Callback(hObject,eventdata));
handles.uimenu_gaussianLPfreqfilter = uimenu(handles.uimenu_lowpassfreqfilter,'tag','gaussianLPfreqfilter',...
    'label','高斯低通...',...
    'callback',@(hObject,eventdata)uimenu_gaussianLPfreqfilter_Callback(hObject,eventdata));
handles.uimenu_highpassfreqfilter = uimenu(handles.uimenu_freqfilter,'tag','uimenu_highpassfreqfilter',...
    'label','高通');
handles.uimenu_idealHPfreqfilter = uimenu(handles.uimenu_highpassfreqfilter,'tag','uimenu_idealHPfreqfilter',...
    'label','理想高通...',...
    'callback',@(hObject,eventdata)uimenu_idealHPfreqfilter_Callback(hObject,eventdata));
handles.uimenu_butterworthHPfreqfilter = uimenu(handles.uimenu_highpassfreqfilter,'tag','uimenu_butterworthHPfreqfilter',...
    'label','布特沃斯高通...',...
    'callback',@(hObject,eventdata)uimenu_butterworthHPfreqfilter_Callback(hObject,eventdata));
handles.uimenu_gaussianHPfreqfilter = uimenu(handles.uimenu_highpassfreqfilter,'tag','gaussianHPfreqfilter',...
    'label','高斯高通...',...
    'callback',@(hObject,eventdata)uimenu_gaussianHPfreqfilter_Callback(hObject,eventdata));


handles.uimenu_noise = uimenu(handles.figure_main,'tag','uimenu_noise',...
    'label','噪声',...
    'callback',@(hObject,eventdata)uimenu_noise_Callback(hObject,eventdata));
handles.uimenu_estnoise = uimenu(handles.uimenu_noise,'tag','uimenu_estnoise',...
    'label','噪声估计',...
    'callback',@(hObject,eventdata)uimenu_estnoise_Callback(hObject,eventdata));
handles.uimenu_addnoise = uimenu(handles.uimenu_noise,'tag','uimenu_addnoise',...
    'label','添加噪声');
handles.uimenu_gausnoise = uimenu(handles.uimenu_addnoise,'tag','uimenu_gausnoise',...
    'label','高斯噪声...',...
    'callback',@(hObject,eventdata)uimenu_gausnoise_Callback(hObject,eventdata));
handles.uimenu_spnoise = uimenu(handles.uimenu_addnoise,'tag','uimenu_spnoise',...
    'label','椒盐噪声...',...
    'callback',@(hObject,eventdata)uimenu_spnoise_Callback(hObject,eventdata));


handles.uitoolbar_main = findall(handles.figure_main,'type','uitoolbar');    % 工具栏

% 去除工具栏中uitogglesplittool
uitogglesplittool_brushing = findall(handles.uitoolbar_main,'type','uitogglesplittool');
delete(uitogglesplittool_brushing);

% 去除工具栏中部分uipushtool
uipushtools = findall(handles.uitoolbar_main,'type','uipushtool');
deleteIndex = [1 2 3 6];
for i=length(deleteIndex):-1:1
    delete(uipushtools(deleteIndex(i)));
    uipushtools(deleteIndex(i)) = [];
end

% 修改工具栏中打开文件按钮的属性
handles.uipushtool_file_open = uipushtools(2);
set(handles.uipushtool_file_open,'tooltipstring','打开图像(Ctrl+O)');
set(handles.uipushtool_file_open,'clickedcallback',@(hObject,eventdata)file_open_Callback(hObject,eventdata));

% 修改工具栏中保存按钮的属性
handles.uipushtool_file_save = uipushtools(1);
set(handles.uipushtool_file_save,'tooltipstring','保存图像(Ctrl+S)');
set(handles.uipushtool_file_save,'clickedcallback',@(hObject,eventdata)file_save_Callback(hObject,eventdata));

% 去除工具栏中部分uitoggletool
uitoggletools = findall(handles.figure_main,'type','uitoggletool');
deleteIndex = [1 2 3 4 9];
for i=length(deleteIndex):-1:1
    delete(uitoggletools(deleteIndex(i)));
    uitoggletools(deleteIndex(i)) = [];
end
handles.uitoggletools = uitoggletools;

load icons;
handles.uipushtool_undo = uipushtool(handles.uitoolbar_main,'tag','uipushtool_undo',...
    'separator','on',...
    'cdata',icons.undo,...
    'tooltipstring','撤销(Ctrl+Z)',...
    'clickedcallback',@(hObject,eventdata)undo_Callback(hObject,eventdata));
handles.uipushtool_redo = uipushtool(handles.uitoolbar_main,'tag','uipushtool_redo',...
    'cdata',icons.redo,...
    'tooltipstring','恢复(Ctrl+Y)',...
    'clickedcallback',@(hObject,eventdata)redo_Callback(hObject,eventdata));
handles.uipushtool_reset = uipushtool(handles.uitoolbar_main,'tag','uipushtool_reset',...
    'separator','on',...
    'cdata',icons.reset,...
    'tooltipstring','重置(Ctrl+R)',...
    'clickedcallback',@(hObject,eventdata)reset_Callback(hObject,eventdata));
setappdata(handles.figure_main,'icons',icons);

% 设置appdata
setappdata(handles.figure_main,'pathName',[]);
setappdata(handles.figure_main,'imageName',[]);
setappdata(handles.figure_main,'countTab',0);    % 用于选项卡命名时的序号
setappdata(handles.figure_main,'countImage',0);    % 用于图像命名时的序号

file_Callback([],[]);
uimenu_edit_Callback([],[]);
uimenu_basicOp_Callback([],[]);


function file_Callback(~,~)
%% 文件
global handles;
% 根据当前是否有选项卡设置一些菜单或工具栏按钮是否可用
if isempty(allchild(handles.uitabgroup_main))
    canOperate = 'off';
else
    canOperate = 'on';
end
set(handles.uimenu_file_close,'enable',canOperate);
set(handles.uimenu_file_save,'enable',canOperate);
set(handles.uimenu_file_saveas,'enable',canOperate);
set(handles.uipushtool_file_save,'enable',canOperate);
for i = 1:length(handles.uitoggletools)
    set(handles.uitoggletools(i),'enable',canOperate);
end


function file_open_Callback(~,~)
%% 打开文件
global handles;
P = getappdata(handles.figure_main,'pathName');    % 目录名称
if isempty(P) || ~exist(P,'dir')    % 目录名称为空或目录不存在则设置为当前目录
    P = './';
end
[fileName,pathName] = uigetfile({'*.bmp;*.jpg;*.jpeg;*.png;*.tif;*.tiff','图像文件(*.bmp,*.jpg,*.jpeg,*.png,*.tif,*.tiff)';...
    '*.*','所有文件(*.*)'},'打开',P);    % 选择文件
if isequal(fileName,0) || isequal(pathName,0)    % 未选择文件
    return;
end
try
    [im,map,alpha] = imread([pathName fileName]);    % 读取图像
    if ~isempty(alpha)
        uiwait(errordlg('暂不支持处理含有透明度参数的图像', '错误','modal'));
        return;
    end
    if ~isempty(map)    % 索引图
        im = ind2rgb(im(:,:,1),map);
    end
    tabs = allchild(handles.uitabgroup_main);    % 所有选项卡
    for i = 1:length(tabs)
        if strcmp([pathName fileName],[getappdata(tabs(i),'pathName') getappdata(tabs(i),'imageName')])    % 相同路径文件已打开
            if isequal(im,getappdata(tabs(i),'originalImage')) && ftell(getappdata(tabs(i),'fileID')) ~= -1    % 原文件未被修改
                set(handles.uitabgroup_main,'selectedtab',tabs(i));
                return;
            else    % 原文件已被修改
                set(tabs(i),'title',[getappdata(tabs(i),'imageName') ' *']);
            end
        end
    end
catch    % 读取图像失败
    uiwait(errordlg('打开图像失败', '错误','modal'));
    return;
end
countTab = getappdata(handles.figure_main,'countTab') + 1;
% 新建选项卡
tab = uitab(handles.uitabgroup_main,'tag',['uitab_image' num2str(countTab)],...
    'title',fileName,...
    'units','normalized');
% 快捷菜单
uicontextmenu_tab = uicontextmenu('tag',['uicontextmenu_tab' num2str(countTab)],...
    'callback',@(hObject,eventdata)uicontextmenu_tab_Callback(hObject,eventdata));
set(tab,'uicontextmenu',uicontextmenu_tab);
% 关闭选项卡菜单
uimenu(uicontextmenu_tab,'tag',['uimenu_tab_close' num2str(countTab)],...
    'label','关闭选项卡',...
    'accelerator','W',...
    'callback',@(hObject,eventdata)uitab_close_Callback(hObject,eventdata));
% 关闭其他选项卡菜单
uimenu_closeothers = uimenu(uicontextmenu_tab,'tag',['uimenu_tab_closeothers' num2str(countTab)],...
    'label','关闭其他选项卡',...
    'callback',@(hObject,eventdata)uitab_closeothers_Callback(hObject,eventdata));
% 关闭所有选项卡菜单
uimenu_closeall = uimenu(uicontextmenu_tab,'tag',['uimenu_tab_closeall' num2str(countTab)],...
    'label','关闭所有选项卡',...
    'callback',@(hObject,eventdata)uitab_closeall_Callback);
% 新建坐标轴
axes_image = axes(tab,'tag',['axes_image' num2str(countTab)],...
    'visible','off',...
    'units','normalized',...
    'position',[0.1 0.1 0.8 0.85]);
try
    axes(axes_image);
    imshow(im);    % 显示图像
catch    % 显示图像失败
    delete(tab);
    uiwait(errordlg('显示图像失败', '错误','modal'));
    return;
end
text_imsize = uicontrol(tab,'tag',['text_imsize' num2str(countTab)],...
    'style','text',...
    'fontsize',12,...
    'HorizontalAlignment','left',...
    'string',[],...
    'units','normalized',...
    'position',[0.01 0 0.2 0.05]);
set(text_imsize,'string',[num2str(size(im,1)) '×' num2str(size(im,2)) '×' num2str(size(im,3))]);
[fileID,errMsg] = fopen([pathName fileName]);    % 打开文件取得读权限以锁定文件
if fileID == -1 || ~isempty(errMsg)
    uiwait(warndlg(['图像已显示，但锁定图像文件' pathName fileName '失败,原因：' errMsg '。'],'警告','modal'));
else
    setappdata(tab,'fileID',fileID);
end
set(handles.uitabgroup_main,'selectedtab',tab);
setappdata(tab,'axes_image',axes_image);
setappdata(tab,'text_imsize',text_imsize);
setappdata(tab,'originalImage',im);
setappdata(tab,'image',im);
setappdata(tab,'uimenu_closeothers',uimenu_closeothers);
setappdata(tab,'uimenu_closeall',uimenu_closeall);

% 用于撤销和恢复的变量
setappdata(tab,'imageStackA',{im});
setappdata(tab,'imageStackB',{});

setappdata(tab,'pathName',pathName);
setappdata(tab,'imageName',fileName);
setappdata(tab,'imageInfo',imfinfo([pathName fileName]));    % 图像文件信息
setappdata(handles.figure_main,'countTab',countTab);
setappdata(handles.figure_main,'pathName',pathName);
setappdata(handles.figure_main,'imageName',fileName);
file_Callback([],[]);    % 文件菜单回调
uimenu_edit_Callback([],[]);


function saved = file_save_Callback(~,~)
%% 保存
global handles;
saved = false;
tab = get(handles.uitabgroup_main,'selectedtab');    % 获取当前选项卡
if ~shouldImSave
    return;
end
P = getappdata(tab,'pathName');
I = getappdata(tab,'imageName');
if ftell(getappdata(tab,'fileID')) == -1 || ~exist([P I],'file')    % 原文件被修改或被删除
    saved = file_saveas_Callback([],[]);    % 另存为
    return;
end
imageInfo = getappdata(tab,'imageInfo');    % 图像文件信息
im = getappdata(tab,'image');    % 图像
try
    imwrite(im,[P I],imageInfo.Format);    % 将图像以原图像格式写入文件
catch    % 写入文件失败
    uiwait(errordlg('保存图像失败', '错误','modal'));
    return;
end
setappdata(tab,'originalImage',im);    % 重置初始图像
set(tab,'title',I);
saved = true;


function saved = file_saveas_Callback(~,~)
%% 另存为
global handles;
saved = false;
tab = get(handles.uitabgroup_main,'selectedtab');    % 获取当前选项卡
shouldImSave;
P = getappdata(tab,'pathName');
if ~exist(P,'dir')    % 目录不存在则设置为当前目录
    P = './';
end
countImage = getappdata(handles.figure_main,'countImage');
I = getappdata(tab,'imageName');

% 设置图像名称
if ftell(getappdata(tab,'fileID')) == -1 || ~exist([P I],'file')
    countImage = countImage + 1;
    I = ['Image ' num2str(countImage) '.png'];
else
    pointIndex = find(I == '.');
    if strcmp(I(length(I)),'.')
        I(length(I)) = [];
    elseif ~isempty(pointIndex)
        I = I(1:pointIndex(length(pointIndex))-1);
    end
    I = [I '.png'];
end

% 选择保存路径
[fileName,pathName] = uiputfile({'*.png','PNG(*.png)';...
    '*.bmp','BMP(*.bmp)';...
    '*.jpg;*.jpeg','JPEG(*.jpg,*.jpeg)';...
    '*.tif;*.tiff','TIFF(*.tif,*.tiff)';...
    '*.*','所有文件(*.*)'},'保存为',[P I]);
if isequal(fileName,0) || isequal(pathName,0)    % 未选择路径
    return;
end
im = getappdata(tab,'image');    % 图像
try
    imwrite(im,[pathName fileName]);    % 将图像写入文件
catch    % 写入失败
    try
        imwrite(im,[pathName fileName],'png');    % 强制以PNG格式写入文件
    catch
        uiwait(errordlg('保存图像失败', '错误','modal'));
        return;
    end
end
try
    fclose(getappdata(tab,'fileID'));    % 关闭文件流
catch
end
set(tab,'title',fileName);    % 设置选项卡标题
[fileID,errMsg] = fopen([pathName fileName],'r');    % 打开文件取得读权限以锁定文件
if fileID == -1 || ~isempty(errMsg)
    uiwait(warndlg(['图像已显示，但锁定图像文件' pathName fileName '失败,原因：' errMsg '。'],'警告','modal'));
else
    setappdata(tab,'fileID',fileID);
end

% 设置appdata
setappdata(tab,'originalImage',im);
setappdata(tab,'pathName',pathName);
setappdata(tab,'imageName',fileName);
setappdata(handles.figure_main,'pathName',pathName);
setappdata(handles.figure_main,'imageName',fileName);
setappdata(handles.figure_main,'countImage',countImage);

saved = true;


function exit_Callback(~,~)
%% 退出
global handles;
if uitab_closeall_Callback([],[])    % 成功关闭所有选项卡
    delete(handles.figure_main);
end




function uimenu_edit_Callback(~,~)
%% 编辑菜单回调
global handles;
tab = get(handles.uitabgroup_main,'selectedtab');
canOperate = {'on','on','on'};
if isempty(tab)
    canOperate = {'off','off','off'};
else
    if length(getappdata(tab,'imageStackA')) == 1
        canOperate{1} = 'off';
    end
    if isempty(getappdata(tab,'imageStackB'))
        canOperate{2} = 'off';
    end
    if isequal(getappdata(tab,'image'),getappdata(tab,'originalImage'))
        canOperate{3} = 'off';
    end
end
set(handles.uimenu_edit_undo,'enable',canOperate{1});
set(handles.uimenu_edit_redo,'enable',canOperate{2});
set(handles.uimenu_edit_reset,'enable',canOperate{3});
set(handles.uipushtool_undo,'enable',canOperate{1});
set(handles.uipushtool_redo,'enable',canOperate{2});
set(handles.uipushtool_reset,'enable',canOperate{3});


function uimenu_basicOp_Callback(~,~)
%% 基本操作菜单回调
global handles;
tab = get(handles.uitabgroup_main,'selectedtab');
canOperate = {'off','off','off'};
if ~isempty(tab)
    im = getappdata(tab,'image');
    if size(im,3) == 3
        canOperate{1} = 'on';
    elseif ~islogical(im)
        canOperate{2} = 'on';
        canOperate{3} = 'on';
    end
end
set(handles.uimenu_gray,'enable',canOperate{1});
set(handles.uimenu_binary,'enable',canOperate{2});
set(handles.uimenu_rgb,'enable',canOperate{3});



function reset_Callback(~,~)
%% 重置
global handles;
tab = get(handles.uitabgroup_main,'selectedtab');
forward(getappdata(tab,'originalImage'));


function uimenu_gray_Callback(~,~)
%% 灰度化菜单回调
global handles;
tab = get(handles.uitabgroup_main,'selectedtab');
turnBusy;
try
    I = rgb2gray(getappdata(tab,'image'));    % 灰度化
catch
    uiwait(errordlg('操作失败。','错误','modal'));
    turnFree;
    return;
end
turnFree;
forward(I);


function uimenu_binary_Callback(~,~)
%% 二值化菜单回调
showChildFigure([0.1 0.3 0.8 0.6],'灰度化');
global F;
text_F_threshold = uicontrol(F.figure_main,'tag','text_F_threshold',...
    'style','text',...
    'fontsize',12,...
    'fontweight','bold',...
    'string','阈值',...
    'units','normalized',...
    'position',[0.1 0.15 0.05 0.04]);
F.slider_threshold = uicontrol(F.figure_main,'tag','slider_F_threshold',...
    'style','slider',...
    'fontsize',12,...
    'min',0,...
    'max',1,...
    'units','normalized',...
    'callback',@(hObject,eventdata)slider_binary_threshold_Callback(hObject,eventdata),...
    'position',[0.17 0.15 0.3 0.04]);
F.edit_threshold = uicontrol(F.figure_main,'tag','edit_F_threshold',...
    'style','edit',...
    'fontsize',12,...
    'units','normalized',...
    'callback',@(hObject,eventdata)edit_binary_threshold_Callback(hObject,eventdata),...
    'position',[0.49 0.15 0.1 0.05]);
align([text_F_threshold,F.slider_threshold,F.edit_threshold],'none','center');    % 对齐组件
set(F.slider_threshold,'value',0.5);
setappdata(F.figure_main,'threshold',0.5);
slider_binary_threshold_Callback([],[]);


function slider_binary_threshold_Callback(~,~)
%% 二值化滑动条回调
global F;
im = getappdata(F.figure_main,'image');
threshold = get(F.slider_threshold,'value');    % 阈值
set(F.edit_threshold,'string',num2str(threshold));
turnBusy;
try
    imtemp = imbinarize(im,threshold);    % 二值化
catch
    uiwait(errordlg('操作失败。','错误','modal'));
    set(F.edit_threshold,'string',num2str(getappdata(F.figure_main,'threshold')));
    set(F.slider_threshold,'value',getappdata(F.figure_main,'threshold'));
    turnFree;
    return;
end
turnFree;
setappdata(F.figure_main,'threshold',threshold);
axes(F.axes_image);
I = imshow(imtemp);
set(F.text_imsize,'string',[num2str(size(imtemp,1)) '×' num2str(size(imtemp,2)) '×' num2str(size(imtemp,3))]);
set(I,'uicontextmenu',F.uicontextmenu_main);
setappdata(F.figure_main,'imtemp',imtemp);


function edit_binary_threshold_Callback(~,~)
%% 二值化文本编辑框回调
global F;
try 
    threshold = str2double(get(F.edit_threshold,'string'));    % 阈值
    assert(threshold>=0 && threshold<=1);
catch
    set(F.edit_threshold,'string',num2str(get(F.slider_threshold,'value')));
    return;
end
set(F.slider_threshold,'value',threshold);
slider_binary_threshold_Callback([],[]);    % 调用二值化滑动条回调



function uimenu_rgb_Callback(~,~)
%% 彩色化菜单回调
showChildFigure([0.1 0.3 0.8 0.6],'彩色化');
global F;

% 红色变换
text_F_rtransf= uicontrol(F.figure_main,'tag','text_F_rtransf',...
    'style','text',...
    'fontsize',12,...
    'fontweight','bold',...
    'string','红色变换',...
    'units','normalized',...
    'position',[0.1 0.15 0.1 0.04]);
F.edit_rtransf = uicontrol(F.figure_main,'tag','edit_rtransf',...
    'style','edit',...
    'fontsize',12,...
    'units','normalized',...
    'callback',@(hObject,eventdata)opt_rgb_btransf_Callback(hObject,eventdata),...
    'position',[0.22 0.15 0.1 0.05]);

% 绿色变换
text_F_gtransf= uicontrol(F.figure_main,'tag','text_F_gtransf',...
    'style','text',...
    'fontsize',12,...
    'fontweight','bold',...
    'string','绿色变换',...
    'units','normalized',...
    'position',[0.37 0.15 0.1 0.04]);
F.edit_gtransf = uicontrol(F.figure_main,'tag','edit_gtransf',...
    'style','edit',...
    'fontsize',12,...
    'units','normalized',...
    'callback',@(hObject,eventdata)opt_rgb_btransf_Callback(hObject,eventdata),...
    'position',[0.49 0.15 0.1 0.05]);

% 蓝色变换
text_F_btransf= uicontrol(F.figure_main,'tag','text_F_btransf',...
    'style','text',...
    'fontsize',12,...
    'fontweight','bold',...
    'string','蓝色变换',...
    'units','normalized',...
    'position',[0.64 0.15 0.1 0.04]);
F.edit_btransf = uicontrol(F.figure_main,'tag','edit_btransf',...
    'style','edit',...
    'fontsize',12,...
    'units','normalized',...
    'callback',@(hObject,eventdata)opt_rgb_btransf_Callback(hObject,eventdata),...
    'position',[0.76 0.15 0.1 0.05]);

align([text_F_rtransf,F.edit_rtransf,text_F_gtransf,F.edit_gtransf,text_F_btransf,F.edit_btransf],...
    'none','center');    % 对齐组件

rtransf = 'abs(sin((1:256) * pi / 128 - pi / 12)) * 255';
set(F.edit_rtransf,'string',rtransf);
setappdata(F.figure_main,'rtransf',rtransf);
gtransf = 'abs(sin((1:256) * pi / 128 - pi / 6)) * 255';
set(F.edit_gtransf,'string',gtransf);
setappdata(F.figure_main,'gtransf',gtransf);
btransf = 'abs(sin((1:256) * pi / 128 - pi / 3)) * 255';
set(F.edit_btransf,'string',btransf);
setappdata(F.figure_main,'btransf',btransf);
opt_rgb_btransf_Callback([],[]);



function opt_rgb_btransf_Callback(~,~)
%% 彩色化
global F;
im = getappdata(F.figure_main,'image');
R = [];
G = [];
B = [];
try
    rtransf = get(F.edit_rtransf,'string');
    eval(['R=',rtransf,';']);
    assert(isnumeric(R) && isreal(R) && isvector(R) && length(R)>=256);
    gtransf = get(F.edit_gtransf,'string');
    eval(['G=',gtransf,';']);
    assert(isnumeric(G) && isreal(G) && isvector(G) && length(G)>=256);
    btransf = get(F.edit_btransf,'string');
    eval(['B=',btransf,';']);
    assert(isnumeric(B) && isreal(B) && isvector(B) && length(B)>=256);
catch
    set(F.edit_rtransf,'string',num2str(getappdata(F.figure_main,'rtransf')));
    set(F.edit_gtransf,'string',num2str(getappdata(F.figure_main,'gtransf')));
    set(F.edit_btransf,'string',num2str(getappdata(F.figure_main,'btransf')));
    return;
end
turnBusy;
try
    imtemp = x_gray2rgb(im,R,G,B);    % 灰度图转彩色图
catch
    uiwait(errordlg('操作失败。','错误','modal'));
    set(F.edit_rtransf,'string',num2str(getappdata(F.figure_main,'rtransf')));
    set(F.edit_gtransf,'string',num2str(getappdata(F.figure_main,'gtransf')));
    set(F.edit_btransf,'string',num2str(getappdata(F.figure_main,'btransf')));
    turnFree;
    return;
end
turnFree;
setappdata(F.figure_main,'rtransf',rtransf);
setappdata(F.figure_main,'gtransf',gtransf);
setappdata(F.figure_main,'btransf',btransf);
axes(F.axes_image);
I = imshow(imtemp);
set(F.text_imsize,'string',[num2str(size(imtemp,1)) '×' num2str(size(imtemp,2)) '×' num2str(size(imtemp,3))]);
set(I,'uicontextmenu',F.uicontextmenu_main);
setappdata(F.figure_main,'imtemp',imtemp);




function uimenu_graytransf_Callback(~,~)
%% 灰度变换菜单回调
global handles;
tab = get(handles.uitabgroup_main,'selectedtab');
canOperate = {'off','off','off','off'};
if ~isempty(tab)
    im = getappdata(tab,'image');
    if size(im,3) == 1 && ~islogical(im)
        canOperate = {'on','on','on','on'};
    end
end
set(handles.uimenu_imreverse,'enable',canOperate{1});
set(handles.uimenu_imlogtransf,'enable',canOperate{2});
set(handles.uimenu_imgammatransf,'enable',canOperate{3});
set(handles.uimenu_imbitplane,'enable',canOperate{4});



function uimenu_imreverse_Callback(~,~)
%% 图像反转菜单回调
global handles;
tab = get(handles.uitabgroup_main,'selectedtab');
turnBusy;
try
    I = x_imreverse(getappdata(tab,'image'));    % 图像反转
catch
    uiwait(errordlg('操作失败。','错误','modal'));
    turnFree;
    return;
end
turnFree;
forward(I);



function uimenu_imlogtransf_Callback(~,~)
%% 对数变换菜单回调
global handles;
tab = get(handles.uitabgroup_main,'selectedtab');
turnBusy;
try
    I = x_imlogtransf(double(getappdata(tab,'image')));    % 对数变换
catch
    uiwait(errordlg('操作失败。','错误','modal'));
    turnFree;
    return;
end
turnFree;
forward(im2uint8(mat2gray(I)));


function uimenu_imgammatransf_Callback(~,~)
%% 幂律变换菜单回调
showChildFigure([0.1 0.3 0.8 0.6],'幂律变换');
global F;
text_F_gamma = uicontrol(F.figure_main,'tag','text_F_gamma',...
    'style','text',...
    'fontsize',12,...
    'fontweight','bold',...
    'string','次幂',...
    'units','normalized',...
    'position',[0.1 0.15 0.05 0.04]);
F.edit_gamma = uicontrol(F.figure_main,'tag','edit_F_gamma',...
    'style','edit',...
    'fontsize',12,...
    'units','normalized',...
    'callback',@(hObject,eventdata)opt_imgammatransf_gamma_Callback(hObject,eventdata),...
    'position',[0.17,0.15 0.1 0.05]);
align([text_F_gamma,F.edit_gamma],'none','center');    % 对齐组件
set(F.edit_gamma,'string',1);
setappdata(F.figure_main,'gamma',1);
opt_imgammatransf_gamma_Callback([],[]);


function opt_imgammatransf_gamma_Callback(~,~)
%% 幂律变换
global F;
im = getappdata(F.figure_main,'image');
try
    gamma = str2double(get(F.edit_gamma,'string'));
catch
    set(F.edit_gamma,'string',num2str(getappdata(F.figure_main,'gamma')));
    return;
end
turnBusy;
try
    imtemp = x_imgammatransf(im,gamma);    % 幂律变换
catch
    uiwait(errordlg('操作失败。','错误','modal'));
    set(F.edit_gamma,'string',num2str(getappdata(F.figure_main,'gamma')));
    turnFree;
    return;
end
turnFree;
set(F.edit_gamma,'string',num2str(gamma));
setappdata(F.figure_main,'gamma',gamma);
axes(F.axes_image);
I = imshow(imtemp);
set(F.text_imsize,'string',[num2str(size(imtemp,1)) '×' num2str(size(imtemp,2)) '×' num2str(size(imtemp,3))]);
set(I,'uicontextmenu',F.uicontextmenu_main);
setappdata(F.figure_main,'imtemp',imtemp);



function uimenu_imbitplane_Callback(~,~)
%% 比特平面分层菜单回调
showChildFigure([0.1 0.3 0.8 0.6],'比特平面分层');
global F;
text_F_bitplane = uicontrol(F.figure_main,'tag','text_F_bitplane',...
    'style','text',...
    'fontsize',12,...
    'fontweight','bold',...
    'string','比特平面',...
    'units','normalized',...
    'position',[0.1 0.15 0.1 0.04]);
F.slider_bitplane= uicontrol(F.figure_main,'tag','slider_F_bitplane',...
    'style','slider',...
    'fontsize',12,...
    'min',1,...
    'max',8,...
    'sliderstep',[1/7 1/7],...
    'units','normalized',...
    'callback',@(hObject,eventdata)slider_imbitplane_plane_Callback(hObject,eventdata),...
    'position',[0.22 0.15 0.3 0.04]);
F.edit_bitplane = uicontrol(F.figure_main,'tag','edit_F_bitplane',...
    'style','edit',...
    'fontsize',12,...
    'units','normalized',...
    'callback',@(hObject,eventdata)edit_imbitplane_plane_Callback(hObject,eventdata),...
    'position',[0.54 0.15 0.1 0.05]);
align([text_F_bitplane,F.slider_bitplane,F.edit_bitplane],'none','center');    % 对齐组件
set(F.slider_bitplane,'value',5);
setappdata(F.figure_main,'plane',5);
slider_imbitplane_plane_Callback([],[]);



function slider_imbitplane_plane_Callback(~,~)
%% 比特平面滑动条回调
global F;
im = getappdata(F.figure_main,'image');
plane = round(get(F.slider_bitplane,'value'));
set(F.slider_bitplane,'value',plane);
set(F.edit_bitplane,'string',num2str(plane));
turnBusy;
try
    imtemp = x_imbitplane(im,plane);    % 比特平面
catch
    uiwait(errordlg('操作失败。','错误','modal'));
    set(F.edit_bitplane,'string',num2str(getappdata(F.figure_main,'plane')));
    set(F.slider_bitplane,'value',getappdata(F.figure_main,'plane'));
    turnFree;
    return;
end
turnFree;
setappdata(F.figure_main,'plane',plane);
axes(F.axes_image);
I = imshow(imtemp);
set(F.text_imsize,'string',[num2str(size(imtemp,1)) '×' num2str(size(imtemp,2)) '×' num2str(size(imtemp,3))]);
set(I,'uicontextmenu',F.uicontextmenu_main);
setappdata(F.figure_main,'imtemp',imtemp);


function edit_imbitplane_plane_Callback(~,~)
%% 比特平面编辑框回调
global F;
try 
    plane = str2double(get(F.edit_threshold,'string'));    % 阈值
    assert(plane>=1 && plane<=8 && round(plane)==plane);
catch
    set(F.edit_bitplane,'string',num2str(get(F.slider_bitplane,'value')));
    return;
end
set(F.slider_bitplane,'value',plane);
slider_imbitplane_plane_Callback([],[])




function uimenu_histprocess_Callback(~,~)
%% 直方图处理菜单回调
global handles;
tab = get(handles.uitabgroup_main,'selectedtab');
canOperate = {'off','off','off','off'};
if ~isempty(tab)
    im = getappdata(tab,'image');
    if size(im,3) == 1 && ~islogical(im)
        canOperate = {'on','on','on','on'};
    elseif size(im,3) == 3
        canOperate = {'on','on','off','off'};
    end
end
set(handles.uimenu_histeq,'enable',canOperate{1});
set(handles.uimenu_histeqwhole,'enable',canOperate{2});
set(handles.uimenu_histeqlocal,'enable',canOperate{3});
set(handles.uimenu_histloceh,'enable',canOperate{4});



function uimenu_histeqwhole_Callback(~,~)
%% 全局直方图均衡菜单回调
global handles;
tab = get(handles.uitabgroup_main,'selectedtab');
turnBusy;
try
    I = x_histeq(getappdata(tab,'image'));    % 直方图均衡
catch
    uiwait(errordlg('操作失败。','错误','modal'));
    turnFree;
    return;
end
turnFree;
forward(I);




function uimenu_histeqlocal_Callback(~,~)
%% 局部直方图均衡菜单回调
showChildFigure([0.1 0.3 0.8 0.6],'局部直方图均衡');
global F;
text_F_method = uicontrol(F.figure_main,'tag','text_F_method',...
    'style','text',...
    'fontsize',12,...
    'fontweight','bold',...
    'string','处理方式',...
    'units','normalized',...
    'position',[0.1 0.15 0.1 0.04]);
F.uibuttongroup_method = uibuttongroup(F.figure_main,'tag','uibuttongroup_method',...
    'title',[],...
    'bordertype','none',...
    'selectionchangedfcn',@(hObject,eventdata)opt_histeqlocal_winsize_Callback(hObject,eventdata),...
    'position',[0.22 0.15 0.25 0.04]);
F.radiobutton_step = uicontrol(F.uibuttongroup_method,'tag','radiobutton_step',...
    'style','radiobutton',...
    'fontsize',12,...
    'fontweight','bold',...
    'string','逐行逐列',...
    'units','normalized',...
    'position',[0 0 0.5 1]);
F.radiobutton_skip = uicontrol(F.uibuttongroup_method,'tag','radiobutton_skip',...
    'style','radiobutton',...
    'fontsize',12,...
    'fontweight','bold',...
    'string','跨行跨列',...
    'units','normalized',...
    'position',[0.5 0 0.5 1]);
text_F_winsize= uicontrol(F.figure_main,'tag','text_F_winsize',...
    'style','text',...
    'fontsize',12,...
    'fontweight','bold',...
    'string','窗口大小',...
    'units','normalized',...
    'position',[0.52 0.15 0.1 0.04]);
F.edit_winsize = uicontrol(F.figure_main,'tag','edit_winsize',...
    'style','edit',...
    'fontsize',12,...
    'units','normalized',...
    'callback',@(hObject,eventdata)opt_histeqlocal_winsize_Callback(hObject,eventdata),...
    'position',[0.64 0.15 0.1 0.05]);
align([text_F_method,F.uibuttongroup_method,text_F_winsize,F.edit_winsize],'none','center');    % 对齐组件
set(F.uibuttongroup_method,'selectedobject',F.radiobutton_step);
set(F.edit_winsize,'string',num2str([3,3]));
setappdata(F.figure_main,'winsize',[3,3]);
opt_histeqlocal_winsize_Callback([],[]);



function opt_histeqlocal_winsize_Callback(~,~)
%% 局部直方图均衡
global F;
im = getappdata(F.figure_main,'image');
try
    winsize = double(str2num(get(F.edit_winsize,'string')));
    assert((numel(winsize)==1 || numel(winsize)==2) && isreal(winsize) && isequal(round(winsize),winsize));
    if numel(winsize) == 1
        winsize = [winsize winsize];
    elseif size(winsize,1) == 2
        winsize = winsize';
    end
    assert(winsize(1)>0 && winsize(2)>0);
catch
    set(F.edit_winsize,'string',num2str(getappdata(F.figure_main,'winsize')));
    return;
end
if get(F.uibuttongroup_method,'selectedobject') == F.radiobutton_step
    method = 'step';
else
    method = 'skip';
end
turnBusy;
try
    imtemp = x_histeq(im,winsize,method);    % 局部直方图均衡
catch
    uiwait(errordlg('操作失败。','错误','modal'));
    set(F.edit_winsize,'string',num2str(getappdata(F.figure_main,'winsize')));
    turnFree;
    return;
end
turnFree;
set(F.edit_winsize,'string',num2str(winsize));
setappdata(F.figure_main,'winsize',winsize);
axes(F.axes_image);
I = imshow(imtemp);
set(F.text_imsize,'string',[num2str(size(imtemp,1)) '×' num2str(size(imtemp,2)) '×' num2str(size(imtemp,3))]);
set(I,'uicontextmenu',F.uicontextmenu_main);
setappdata(F.figure_main,'imtemp',imtemp);



function uimenu_histloceh_Callback(~,~)
%% 使用直方图的图像局部增强菜单回调
showChildFigure([0.1 0.3 0.8 0.6],'直方图局部增强');
global F;
text_F_winsize= uicontrol(F.figure_main,'tag','text_F_winsize',...
    'style','text',...
    'fontsize',12,...
    'fontweight','bold',...
    'string','窗口大小(奇数)',...
    'units','normalized',...
    'position',[0.1 0.15 0.15 0.04]);
F.edit_winsize = uicontrol(F.figure_main,'tag','edit_winsize',...
    'style','edit',...
    'fontsize',12,...
    'units','normalized',...
    'callback',@(hObject,eventdata)opt_histloceh_winsize_Callback(hObject,eventdata),...
    'position',[0.27 0.15 0.1 0.05]);
align([text_F_winsize,F.edit_winsize],'none','center');    % 对齐组件
set(F.edit_winsize,'string','3');
setappdata(F.figure_main,'winsize',3);
opt_histloceh_winsize_Callback([],[]);



function opt_histloceh_winsize_Callback(~,~)
%% 使用直方图的图像局部增强
global F;
im = getappdata(F.figure_main,'image');
try
    winsize = str2double(get(F.edit_winsize,'string'));
    assert(numel(winsize)==1 && isreal(winsize) && round(winsize) == winsize &&...
        winsize >= 1 && winsize <= min(size(im)) && mod(winsize,2) == 1);
catch
    set(F.edit_winsize,'string',num2str(getappdata(F.figure_main,'winsize')));
    return;
end
turnBusy;
try
    imtemp = x_histloceh(im,winsize);    % 图像局部增强
catch
    uiwait(errordlg('操作失败。','错误','modal'));
    set(F.edit_winsize,'string',num2str(getappdata(F.figure_main,'winsize')));
    turnFree;
    return;
end
turnFree;
set(F.edit_winsize,'string',num2str(winsize));
setappdata(F.figure_main,'winsize',winsize);
axes(F.axes_image);
I = imshow(imtemp);
set(F.text_imsize,'string',[num2str(size(imtemp,1)) '×' num2str(size(imtemp,2)) '×' num2str(size(imtemp,3))]);
set(I,'uicontextmenu',F.uicontextmenu_main);
setappdata(F.figure_main,'imtemp',imtemp);




function uimenu_affinetransf_Callback(~,~)
%% 仿射变换菜单回调
global handles;
tab = get(handles.uitabgroup_main,'selectedtab');
if isempty(tab)
    canOperate = 'off';
else
    canOperate = 'on';
end
set(handles.uimenu_imresize,'enable',canOperate);
set(handles.uimenu_imrotate,'enable',canOperate);
set(handles.uimenu_imsheartransf,'enable',canOperate);


function uimenu_imresize_Callback(~,~)
%% 比例变换菜单回调
showChildFigure([0.1 0.3 0.8 0.6],'缩放变换');
global F;
text_F_scale= uicontrol(F.figure_main,'tag','text_F_scale',...
    'style','text',...
    'fontsize',12,...
    'fontweight','bold',...
    'string','缩放比例',...
    'units','normalized',...
    'position',[0.1 0.15 0.1 0.04]);
F.edit_scale = uicontrol(F.figure_main,'tag','edit_scale',...
    'style','edit',...
    'fontsize',12,...
    'units','normalized',...
    'callback',@(hObject,eventdata)opt_imresize_scale_Callback(hObject,eventdata),...
    'position',[0.22 0.15 0.1 0.05]);
align([text_F_scale,F.edit_scale],'none','center');    % 对齐组件
set(F.edit_scale,'string',num2str([1 1]));
setappdata(F.figure_main,'scale',[1 1]);
opt_imresize_scale_Callback([],[]);


function opt_imresize_scale_Callback(~,~)
%% 比例变换
global F;
im = getappdata(F.figure_main,'image');
try
    scale = double(str2num(get(F.edit_scale,'string')));
    assert((numel(scale)==1 || numel(scale)==2) && isreal(scale));
    if numel(scale)==1
        assert(scale>0);
        scale = [scale scale];
    else
        assert(scale(1)>0 && scale(2)>0);
        if size(scale,1)==2
            scale = scale';
        end
    end
catch
    set(F.edit_scale,'string',num2str(getappdata(F.figure_main,'scale')));
    return;
end
turnBusy;
try
    imtemp = x_imresize(im,scale(1),scale(2));    % 比例变换
catch
    uiwait(errordlg('操作失败。','错误','modal'));
    set(F.edit_scale,'string',num2str(getappdata(F.figure_main,'scale')));
    turnFree;
    return;
end
turnFree;
set(F.edit_scale,'string',num2str(scale));
setappdata(F.figure_main,'scale',scale);
axes(F.axes_image);
I = imshow(imtemp);
set(F.text_imsize,'string',[num2str(size(imtemp,1)) '×' num2str(size(imtemp,2)) '×' num2str(size(imtemp,3))]);
set(I,'uicontextmenu',F.uicontextmenu_main);
setappdata(F.figure_main,'imtemp',imtemp);




function uimenu_imrotate_Callback(~,~)
%% 旋转变换菜单回调
showChildFigure([0.1 0.3 0.8 0.6],'旋转变换');
global F;
text_F_angle= uicontrol(F.figure_main,'tag','text_F_angle',...
    'style','text',...
    'fontsize',12,...
    'fontweight','bold',...
    'string','角度',...
    'units','normalized',...
    'position',[0.1 0.15 0.05 0.04]);
F.edit_angle = uicontrol(F.figure_main,'tag','edit_angle',...
    'style','edit',...
    'fontsize',12,...
    'units','normalized',...
    'callback',@(hObject,eventdata)opt_imrotate_angle_Callback(hObject,eventdata),...
    'position',[0.17 0.15 0.1 0.05]);
F.checkbox_clip = uicontrol(F.figure_main,'tag','uicheckbox_clip',...
    'style','checkbox',...
    'fontsize',12,...
    'fontweight','bold',...
    'string','裁剪',...
    'callback',@(hObject,eventdata)checkbox_imrotate_clip_Callback(hObject,eventdata),...
    'units','normalized',...
    'position',[0.32 0.15 0.1 0.05]);
F.text_rotcenter= uicontrol(F.figure_main,'tag','text_rotcenter',...
    'style','text',...
    'fontsize',12,...
    'fontweight','bold',...
    'string','旋转中心',...
    'units','normalized',...
    'position',[0.47 0.15 0.1 0.04]);
F.edit_rotcenter = uicontrol(F.figure_main,'tag','edit_rotcenter',...
    'style','edit',...
    'fontsize',12,...
    'units','normalized',...
    'callback',@(hObject,eventdata)opt_imrotate_angle_Callback(hObject,eventdata),...
    'position',[0.59 0.15 0.1 0.05]);
align([text_F_angle,F.edit_angle,F.checkbox_clip,F.text_rotcenter,F.edit_rotcenter],'none','center');    % 对齐组件
set(F.edit_angle,'string','0');
set(F.checkbox_clip,'value',1);
set(F.edit_rotcenter,'string',num2str([1 1]));
setappdata(F.figure_main,'angle',0);
setappdata(F.figure_main,'rotcenter',[1 1]);
opt_imrotate_angle_Callback([],[]);


function checkbox_imrotate_clip_Callback(~,~)
%% 裁剪复选框回调
global F;
if get(F.checkbox_clip,'value') == 1
    visible = 'on';
else
    visible = 'off';
end
set(F.edit_rotcenter,'visible',visible);
set(F.text_rotcenter,'visible',visible);
opt_imrotate_angle_Callback([],[]);



function opt_imrotate_angle_Callback(~,~)
%% 旋转变换
global F;
im = getappdata(F.figure_main,'image');
clip = get(F.checkbox_clip,'value') == 1;
try
    angle = str2double(get(F.edit_angle,'string'));
    assert(numel(angle)==1 && isreal(angle));
    if clip
        rotcenter = double(str2num(get(F.edit_rotcenter,'string')));
        assert((numel(rotcenter)==1 || numel(rotcenter)==2) && isreal(rotcenter));
        if numel(rotcenter)==1
            rotcenter = [rotcenter rotcenter];
        else
            if size(rotcenter,1)==2
                rotcenter = rotcenter';
            end
        end
    end
catch
    set(F.edit_angle,'string',num2str(getappdata(F.figure_main,'angle')));
    set(F.edit_rotcenter,'string',num2str(getappdata(F.figure_main,'rotcenter')));
    return;
end
if clip
    set(F.edit_rotcenter,'string',num2str(rotcenter));
    setappdata(F.figure_main,'rotcenter',rotcenter);
end
turnBusy;
try
    % 旋转变换
    if clip
        imtemp = x_imrotate(im,angle,rotcenter(1),rotcenter(2));
    else
        imtemp = x_imrotate(im,angle);
    end
catch
    uiwait(errordlg('操作失败。','错误','modal'));
    set(F.edit_angle,'string',num2str(getappdata(F.figure_main,'angle')));
    set(F.edit_rotcenter,'string',num2str(getappdata(F.figure_main,'rotcenter')));
    turnFree;
    return;
end
turnFree;
set(F.edit_angle,'string',num2str(angle));
setappdata(F.figure_main,'angle',angle);
axes(F.axes_image);
I = imshow(imtemp);
set(F.text_imsize,'string',[num2str(size(imtemp,1)) '×' num2str(size(imtemp,2)) '×' num2str(size(imtemp,3))]);
set(I,'uicontextmenu',F.uicontextmenu_main);
setappdata(F.figure_main,'imtemp',imtemp);




function uimenu_imsheartransf_Callback(~,~)
%% 剪切变换菜单回调
showChildFigure([0.1 0.3 0.8 0.6],'剪切变换');
global F;
text_F_shear= uicontrol(F.figure_main,'tag','text_F_shear',...
    'style','text',...
    'fontsize',12,...
    'fontweight','bold',...
    'string','剪切偏移',...
    'units','normalized',...
    'position',[0.1 0.15 0.1 0.04]);
F.edit_shear = uicontrol(F.figure_main,'tag','edit_shear',...
    'style','edit',...
    'fontsize',12,...
    'units','normalized',...
    'callback',@(hObject,eventdata)opt_imsheartransf_shear_Callback(hObject,eventdata),...
    'position',[0.22 0.15 0.1 0.05]);
align([text_F_shear,F.edit_shear],'none','center');    % 对齐组件
set(F.edit_shear,'string',num2str([0.1 0.1]));
setappdata(F.figure_main,'shear',[0.1 0.1]);
opt_imsheartransf_shear_Callback([],[]);




function opt_imsheartransf_shear_Callback(~,~)
%% 剪切变换
global F;
im = getappdata(F.figure_main,'image');
try
    shear = double(str2num(get(F.edit_shear,'string')));
    assert((numel(shear)==1 || numel(shear)==2) && isreal(shear));
    if numel(shear)==1
        shear = [shear shear];
    else
        if size(shear,1)==2
            shear = shear';
        end
    end
    assert(shear(1)*shear(2)~=1);
catch
    set(F.edit_shear,'string',num2str(getappdata(F.figure_main,'shear')));
    return;
end
turnBusy;
try
    imtemp = x_imsheartransf(im,shear(1),shear(2));    % 剪切变换
catch
    uiwait(errordlg('操作失败。','错误','modal'));
    set(F.edit_shear,'string',num2str(getappdata(F.figure_main,'shear')));
    turnFree;
    return;
end
turnFree;
set(F.edit_shear,'string',num2str(shear));
setappdata(F.figure_main,'shear',shear);
axes(F.axes_image);
I = imshow(imtemp);
set(F.text_imsize,'string',[num2str(size(imtemp,1)) '×' num2str(size(imtemp,2)) '×' num2str(size(imtemp,3))]);
set(I,'uicontextmenu',F.uicontextmenu_main);
setappdata(F.figure_main,'imtemp',imtemp);




function uimenu_spafilter_Callback(~,~)
%% 空间滤波菜单回调
global handles;
tab = get(handles.uitabgroup_main,'selectedtab');
canOperate = {'off','off','off'};
if ~isempty(tab)
    im = getappdata(tab,'image');
    if islogical(im)
        canOperate = {'on','on','off'};
    else
        canOperate = {'on','on','on'};
    end
end
set(handles.uimenu_fuzspafilter,'enable',canOperate{1});
set(handles.uimenu_sharpspafilter,'enable',canOperate{2});
set(handles.uimenu_spafilter_others,'enable',canOperate{3});


function uimenu_fuzspafilter_mean_Callback(~,~)
%% 均值平滑滤波菜单回调
uimenu_fuzspafilter('meanval');




function uimenu_fuzspafilter_gaussian_Callback(~,~)
%% 高斯平滑滤波菜单回调
uimenu_fuzspafilter('gaussian');




function uimenu_fuzspafilter(method)
%% 平滑滤波
if strcmp(method,'meanval')
    title = '均值空间滤波';
else
    title = '高斯空间滤波';
end
showChildFigure([0.1 0.3 0.8 0.6],title);
global F;
setappdata(F.figure_main,'method',method);
text_F_winsize= uicontrol(F.figure_main,'tag','text_F_winsize',...
    'style','text',...
    'fontsize',12,...
    'fontweight','bold',...
    'string','窗口大小',...
    'units','normalized',...
    'position',[0.1 0.15 0.1 0.04]);
F.edit_winsize = uicontrol(F.figure_main,'tag','edit_winsize',...
    'style','edit',...
    'fontsize',12,...
    'units','normalized',...
    'callback',@(hObject,eventdata)opt_fuzspafilter_winsize_Callback(hObject,eventdata),...
    'position',[0.22 0.15 0.1 0.05]);
align([text_F_winsize,F.edit_winsize],'none','center');    % 对齐组件
set(F.edit_winsize,'string',num2str([3 3]));
setappdata(F.figure_main,'winsize',[3 3]);
opt_fuzspafilter_winsize_Callback([],[]);



function opt_fuzspafilter_winsize_Callback(~,~)
%% 平滑滤波
global F;
im = getappdata(F.figure_main,'image');
try
    winsize = double(str2num(get(F.edit_winsize,'string')));
    assert((numel(winsize)==1 || numel(winsize)==2) && isreal(winsize) && isequal(winsize,round(winsize)));
    if numel(winsize)==1
        assert(winsize>0);
        winsize = [winsize winsize];
    else
        assert(winsize(1)>0 && winsize(2)>0);
        if size(winsize,1)==2
            winsize = winsize';
        end
    end
catch
    set(F.edit_winsize,'string',num2str(getappdata(F.figure_main,'winsize')));
    return;
end
turnBusy;
try
    if strcmp(getappdata(F.figure_main,'method'),'meanval')
        imtemp = x_imfilter(im,fspecial('average',winsize));
    else
        imtemp = x_imfilter(im,fspecial('gaussian',winsize));
    end
catch
    uiwait(errordlg('操作失败。','错误','modal'));
    set(F.edit_winsize,'string',num2str(getappdata(F.figure_main,'winsize')));
    turnFree;
    return;
end
turnFree;
set(F.edit_winsize,'string',num2str(winsize));
setappdata(F.figure_main,'winsize',winsize);
axes(F.axes_image);
I = imshow(imtemp);
set(F.text_imsize,'string',[num2str(size(imtemp,1)) '×' num2str(size(imtemp,2)) '×' num2str(size(imtemp,3))]);
set(I,'uicontextmenu',F.uicontextmenu_main);
setappdata(F.figure_main,'imtemp',imtemp);




function uimenu_sharpspafilter_firstder_Callback(~,~)
%% 一阶微分锐化菜单回调
global handles;
tab = get(handles.uitabgroup_main,'selectedtab');
im = getappdata(tab,'image');
T = fspecial('sobel');
try
    turnBusy;
    I = im + x_imfilter(im,T) + x_imfilter(im,T');
    turnFree;
catch
    uiwait(errordlg('操作失败。','错误','modal'));
    turnFree;
    return;
end
forward(I);



function uimenu_sharpspafilter_secondder_Callback(~,~)
%% 二阶微分锐化菜单回调
global handles;
tab = get(handles.uitabgroup_main,'selectedtab');
im = getappdata(tab,'image');
T = ones(3);
T(2,2) = -8;
try
    turnBusy;
    I = im - x_imfilter(im,T);
    turnFree;
catch
    uiwait(errordlg('操作失败。','错误','modal'));
    turnFree;
    return;
end
forward(I);



function uimenu_iminvharmeanfilter_Callback(~,~)
%% 逆谐波均值滤波菜单回调
showChildFigure([0.1 0.3 0.8 0.6],'逆谐波均值滤波');
global F;
text_F_winsize= uicontrol(F.figure_main,'tag','text_F_winsize',...
    'style','text',...
    'fontsize',12,...
    'fontweight','bold',...
    'string','窗口大小',...
    'units','normalized',...
    'position',[0.1 0.15 0.1 0.04]);
F.edit_winsize = uicontrol(F.figure_main,'tag','edit_winsize',...
    'style','edit',...
    'fontsize',12,...
    'units','normalized',...
    'callback',@(hObject,eventdata)opt_iminvharmeanfilter_power_Callback(hObject,eventdata),...
    'position',[0.22 0.15 0.1 0.05]);
text_F_power = uicontrol(F.figure_main,'tag','text_F_power',...
    'style','text',...
    'fontsize',12,...
    'fontweight','bold',...
    'string','阶数',...
    'units','normalized',...
    'position',[0.37 0.15 0.05 0.04]);
F.edit_power = uicontrol(F.figure_main,'tag','edit_power',...
    'style','edit',...
    'fontsize',12,...
    'units','normalized',...
    'callback',@(hObject,eventdata)opt_iminvharmeanfilter_power_Callback(hObject,eventdata),...
    'position',[0.44 0.15 0.1 0.05]);
align([text_F_winsize,text_F_power,F.edit_winsize,F.edit_power],'none','center');    % 对齐组件
set(F.edit_winsize,'string',num2str([3 3]));
setappdata(F.figure_main,'winsize',[3 3]);
set(F.edit_power,'string','1');
setappdata(F.figure_main,'power',1);
opt_iminvharmeanfilter_power_Callback([],[]);



function opt_iminvharmeanfilter_power_Callback(~,~)
%% 逆谐波均值滤波
global F;
im = getappdata(F.figure_main,'image');
try
    winsize = double(str2num(get(F.edit_winsize,'string')));
    assert((numel(winsize)==1 || numel(winsize)==2) && isreal(winsize) && isequal(winsize,round(winsize)));
    if numel(winsize)==1
        assert(winsize>0);
        winsize = [winsize winsize];
    else
        assert(winsize(1)>0 && winsize(2)>0);
        if size(winsize,1)==2
            winsize = winsize';
        end
    end
    power = str2double(get(F.edit_power,'string'));
    assert(isreal(power));
catch
    set(F.edit_winsize,'string',num2str(getappdata(F.figure_main,'winsize')));
    set(F.edit_power,'string',num2str(getappdata(F.figure_main,'power')));
    return;
end
turnBusy;
try
    imtemp = x_iminvharmeanfilter(im,winsize,power);    % 逆谐波均值滤波
catch
    uiwait(errordlg('操作失败。','错误','modal'));
    set(F.edit_winsize,'string',num2str(winsize));
    set(F.edit_power,'string',num2str(getappdata(F.figure_main,'power')));
    turnFree;
    return;
end
turnFree;
set(F.edit_winsize,'string',num2str(winsize));
set(F.edit_power,'string',num2str(power));
setappdata(F.figure_main,'winsize',winsize);
setappdata(F.figure_main,'power',power);
axes(F.axes_image);
I = imshow(imtemp);
set(F.text_imsize,'string',[num2str(size(imtemp,1)) '×' num2str(size(imtemp,2)) '×' num2str(size(imtemp,3))]);
set(I,'uicontextmenu',F.uicontextmenu_main);
setappdata(F.figure_main,'imtemp',imtemp);




function uimenu_immodalpmeanfilter_Callback(~,~)
%% 修正的阿尔法均值滤波菜单回调
showChildFigure([0.1 0.3 0.8 0.6],'修正的阿尔法均值滤波');
global F;
text_F_winsize= uicontrol(F.figure_main,'tag','text_F_winsize',...
    'style','text',...
    'fontsize',12,...
    'fontweight','bold',...
    'string','窗口大小',...
    'units','normalized',...
    'position',[0.1 0.15 0.1 0.04]);
F.edit_winsize = uicontrol(F.figure_main,'tag','edit_winsize',...
    'style','edit',...
    'fontsize',12,...
    'units','normalized',...
    'callback',@(hObject,eventdata)opt_immodalpmeanfilter_drop_Callback(hObject,eventdata),...
    'position',[0.22 0.15 0.1 0.05]);
text_F_drop = uicontrol(F.figure_main,'tag','text_F_drop',...
    'style','text',...
    'fontsize',12,...
    'fontweight','bold',...
    'string','去除像素个数',...
    'units','normalized',...
    'position',[0.37 0.15 0.15 0.04]);
F.edit_drop = uicontrol(F.figure_main,'tag','edit_drop',...
    'style','edit',...
    'fontsize',12,...
    'units','normalized',...
    'callback',@(hObject,eventdata)opt_immodalpmeanfilter_drop_Callback(hObject,eventdata),...
    'position',[0.54 0.15 0.1 0.05]);
align([text_F_winsize,text_F_drop,F.edit_winsize,F.edit_drop],'none','center');    % 对齐组件
set(F.edit_winsize,'string',num2str([3 3]));
setappdata(F.figure_main,'winsize',[3 3]);
set(F.edit_drop,'string','1');
setappdata(F.figure_main,'drop',1);
opt_immodalpmeanfilter_drop_Callback([],[]);



function opt_immodalpmeanfilter_drop_Callback(~,~)
%% 修正的阿尔法均值滤波
global F;
im = getappdata(F.figure_main,'image');
try
    winsize = double(str2num(get(F.edit_winsize,'string')));
    assert((numel(winsize)==1 || numel(winsize)==2) && isreal(winsize) && isequal(winsize,round(winsize)));
    if numel(winsize)==1
        assert(winsize>0);
        winsize = [winsize winsize];
    else
        assert(winsize(1)>0 && winsize(2)>0);
        if size(winsize,1)==2
            winsize = winsize';
        end
    end
    drop = str2double(get(F.edit_drop,'string'));
    assert(isreal(drop) && isequal(round(drop),drop) && drop>=0 && drop < winsize(1)*winsize(2));
catch
    set(F.edit_winsize,'string',num2str(getappdata(F.figure_main,'winsize')));
    set(F.edit_drop,'string',num2str(getappdata(F.figure_main,'drop')));
    return;
end
turnBusy;
try
    imtemp = x_immodalpmeanfilter(im,winsize,drop);    % 修正的阿尔法均值滤波
catch
    uiwait(errordlg('操作失败。','错误','modal'));
    set(F.edit_winsize,'string',num2str(winsize));
    set(F.edit_drop,'string',num2str(getappdata(F.figure_main,'power')));
    turnFree;
    return;
end
turnFree;
set(F.edit_winsize,'string',num2str(winsize));
set(F.edit_drop,'string',num2str(drop));
setappdata(F.figure_main,'winsize',winsize);
setappdata(F.figure_main,'drop',drop);
axes(F.axes_image);
I = imshow(imtemp);
set(F.text_imsize,'string',[num2str(size(imtemp,1)) '×' num2str(size(imtemp,2)) '×' num2str(size(imtemp,3))]);
set(I,'uicontextmenu',F.uicontextmenu_main);
setappdata(F.figure_main,'imtemp',imtemp);




function uimenu_imadprednoisfilter_Callback(~,~)
%% 自适应局部降低噪声滤波菜单回调
showChildFigure([0.1 0.3 0.8 0.6],'自适应局部降低噪声滤波');
global F;
text_F_winsize= uicontrol(F.figure_main,'tag','text_F_winsize',...
    'style','text',...
    'fontsize',12,...
    'fontweight','bold',...
    'string','窗口大小',...
    'units','normalized',...
    'position',[0.1 0.15 0.1 0.04]);
F.edit_winsize = uicontrol(F.figure_main,'tag','edit_winsize',...
    'style','edit',...
    'fontsize',12,...
    'units','normalized',...
    'callback',@(hObject,eventdata)opt_imadprednoisfilter_globalvar_Callback(hObject,eventdata),...
    'position',[0.22 0.15 0.1 0.05]);
text_F_globalvar = uicontrol(F.figure_main,'tag','text_F_globalvar',...
    'style','text',...
    'fontsize',12,...
    'fontweight','bold',...
    'string','全局噪声方差',...
    'units','normalized',...
    'position',[0.37 0.15 0.15 0.04]);
F.edit_globalvar = uicontrol(F.figure_main,'tag','edit_globalvar',...
    'style','edit',...
    'fontsize',12,...
    'units','normalized',...
    'callback',@(hObject,eventdata)opt_imadprednoisfilter_globalvar_Callback(hObject,eventdata),...
    'position',[0.54 0.15 0.1 0.05]);
align([text_F_winsize,text_F_globalvar,F.edit_winsize,F.edit_globalvar],'none','center');    % 对齐组件
set(F.edit_winsize,'string',num2str([3 3]));
setappdata(F.figure_main,'winsize',[3 3]);
set(F.edit_globalvar,'string','0');
setappdata(F.figure_main,'globalvar',0);
opt_imadprednoisfilter_globalvar_Callback([],[]);



function opt_imadprednoisfilter_globalvar_Callback(~,~)
%% 自适应局部降低噪声滤波
global F;
im = getappdata(F.figure_main,'image');
try
    winsize = double(str2num(get(F.edit_winsize,'string')));
    assert((numel(winsize)==1 || numel(winsize)==2) && isreal(winsize) && isequal(winsize,round(winsize)));
    if numel(winsize)==1
        assert(winsize>0);
        winsize = [winsize winsize];
    else
        assert(winsize(1)>0 && winsize(2)>0);
        if size(winsize,1)==2
            winsize = winsize';
        end
    end
    globalvar = str2double(get(F.edit_globalvar,'string'));
    assert(isreal(globalvar));
catch
    set(F.edit_winsize,'string',num2str(getappdata(F.figure_main,'winsize')));
    set(F.edit_globalvar,'string',num2str(getappdata(F.figure_main,'globalvar')));
    return;
end
turnBusy;
try
    imtemp = x_imadprednoisfilter(im,winsize,globalvar);    % 自适应局部降低噪声滤波
catch
    uiwait(errordlg('操作失败。','错误','modal'));
    set(F.edit_winsize,'string',num2str(winsize));
    set(F.edit_globalvar,'string',num2str(getappdata(F.figure_main,'globalvar')));
    turnFree;
    return;
end
turnFree;
set(F.edit_winsize,'string',num2str(winsize));
set(F.edit_globalvar,'string',num2str(globalvar));
setappdata(F.figure_main,'winsize',winsize);
setappdata(F.figure_main,'globalvar',globalvar);
axes(F.axes_image);
I = imshow(imtemp);
set(F.text_imsize,'string',[num2str(size(imtemp,1)) '×' num2str(size(imtemp,2)) '×' num2str(size(imtemp,3))]);
set(I,'uicontextmenu',F.uicontextmenu_main);
setappdata(F.figure_main,'imtemp',imtemp);




function uimenu_imadpmedianfilter_Callback(~,~)
%% 自适应中值滤波菜单回调
showChildFigure([0.1 0.3 0.8 0.6],'自适应中值滤波');
global F;
text_F_winsize= uicontrol(F.figure_main,'tag','text_F_winsize',...
    'style','text',...
    'fontsize',12,...
    'fontweight','bold',...
    'string','窗口大小',...
    'units','normalized',...
    'position',[0.1 0.15 0.1 0.04]);
F.edit_winsize = uicontrol(F.figure_main,'tag','edit_winsize',...
    'style','edit',...
    'fontsize',12,...
    'units','normalized',...
    'callback',@(hObject,eventdata)opt_imadpmedianfilter_winincr_Callback(hObject,eventdata),...
    'position',[0.22 0.15 0.1 0.05]);
text_F_winincr = uicontrol(F.figure_main,'tag','text_F_winincr',...
    'style','text',...
    'fontsize',12,...
    'fontweight','bold',...
    'string','窗口大小最大增量',...
    'units','normalized',...
    'position',[0.37 0.15 0.2 0.04]);
F.edit_winincr = uicontrol(F.figure_main,'tag','edit_winincr',...
    'style','edit',...
    'fontsize',12,...
    'units','normalized',...
    'callback',@(hObject,eventdata)opt_imadpmedianfilter_winincr_Callback(hObject,eventdata),...
    'position',[0.59 0.15 0.1 0.05]);
align([text_F_winsize,text_F_winincr,F.edit_winsize,F.edit_winincr],'none','center');    % 对齐组件
set(F.edit_winsize,'string',num2str([3 3]));
setappdata(F.figure_main,'winsize',[3 3]);
set(F.edit_winincr,'string','1');
setappdata(F.figure_main,'winincr',1);
opt_imadpmedianfilter_winincr_Callback([],[]);



function opt_imadpmedianfilter_winincr_Callback(~,~)
%% 自适应中值滤波
global F;
im = getappdata(F.figure_main,'image');
try
    winsize = double(str2num(get(F.edit_winsize,'string')));
    assert((numel(winsize)==1 || numel(winsize)==2) && isreal(winsize) && isequal(winsize,round(winsize)));
    if numel(winsize)==1
        assert(winsize>0);
        winsize = [winsize winsize];
    else
        assert(winsize(1)>0 && winsize(2)>0);
        if size(winsize,1)==2
            winsize = winsize';
        end
    end
    winincr = str2double(get(F.edit_winincr,'string'));
    assert(isreal(winincr) && winincr>=0 && isequal(winincr,round(winincr)));
catch
    set(F.edit_winsize,'string',num2str(getappdata(F.figure_main,'winsize')));
    set(F.edit_winincr,'string',num2str(getappdata(F.figure_main,'winincr')));
    return;
end
turnBusy;
try
    imtemp = x_imadpmedianfilter(im,winsize,winincr);    % 自适应中值滤波
catch
    uiwait(errordlg('操作失败。','错误','modal'));
    set(F.edit_winsize,'string',num2str(winsize));
    set(F.edit_winincr,'string',num2str(getappdata(F.figure_main,'winincr')));
    turnFree;
    return;
end
turnFree;
set(F.edit_winsize,'string',num2str(winsize));
set(F.edit_winincr,'string',num2str(winincr));
setappdata(F.figure_main,'winsize',winsize);
setappdata(F.figure_main,'winincr',winincr);
axes(F.axes_image);
I = imshow(imtemp);
set(F.text_imsize,'string',[num2str(size(imtemp,1)) '×' num2str(size(imtemp,2)) '×' num2str(size(imtemp,3))]);
set(I,'uicontextmenu',F.uicontextmenu_main);
setappdata(F.figure_main,'imtemp',imtemp);



function uimenu_freqfilter_Callback(~,~)
%% 频域滤波菜单回调
global handles;
tab = get(handles.uitabgroup_main,'selectedtab');
canOperate = {'off','off'};
if ~isempty(tab)
    im = getappdata(tab,'image');
    if size(im,3)==1 && ~islogical(im)
        canOperate = {'on','on'};
    end
end
set(handles.uimenu_lowpassfreqfilter,'enable',canOperate{1});
set(handles.uimenu_highpassfreqfilter,'enable',canOperate{2});



function uimenu_idealLPfreqfilter_Callback(~,~)
%% 理想低通滤波菜单回调
showChildFigure([0.1 0.3 0.8 0.6],'理想低通滤波');
global F;
text_F_freq = uicontrol(F.figure_main,'tag','text_F_freq',...
    'style','text',...
    'fontsize',12,...
    'fontweight','bold',...
    'string','截止频率',...
    'units','normalized',...
    'position',[0.1 0.15 0.1 0.04]);
F.edit_freq = uicontrol(F.figure_main,'tag','edit_freq',...
    'style','edit',...
    'fontsize',12,...
    'units','normalized',...
    'callback',@(hObject,eventdata)opt_idealLPfreqfilter_freq_Callback(hObject,eventdata),...
    'position',[0.22 0.15 0.1 0.05]);
align([text_F_freq,F.edit_freq],'none','center');
set(F.edit_freq,'string','100');
setappdata(F.figure_main,'freq',100);
opt_idealLPfreqfilter_freq_Callback([],[]);



function opt_idealLPfreqfilter_freq_Callback(~,~)
%% 理想低通滤波
global F;
im = getappdata(F.figure_main,'image');
try
    freq = str2double(get(F.edit_freq,'string'));
    assert(isreal(freq) && freq>=0);
catch
    set(F.edit_freq,'string',num2str(getappdata(F.figure_main,'freq')));
    return;
end
turnBusy;
try
    imtemp = x_imfreqfilter(im,'idealL',freq);    % 理想低通滤波
catch
    uiwait(errordlg('操作失败。','错误','modal'));
    set(F.edit_freq,'string',num2str(getappdata(F.figure_main,'freq')));
    turnFree;
    return;
end
turnFree;
set(F.edit_freq,'string',num2str(freq));
setappdata(F.figure_main,'freq',freq);
axes(F.axes_image);
I = imshow(imtemp);
set(F.text_imsize,'string',[num2str(size(imtemp,1)) '×' num2str(size(imtemp,2)) '×' num2str(size(imtemp,3))]);
set(I,'uicontextmenu',F.uicontextmenu_main);
setappdata(F.figure_main,'imtemp',imtemp);




function uimenu_idealHPfreqfilter_Callback(~,~)
%% 理想高通滤波菜单回调
showChildFigure([0.1 0.3 0.8 0.6],'理想高通滤波');
global F;
text_F_freq = uicontrol(F.figure_main,'tag','text_F_freq',...
    'style','text',...
    'fontsize',12,...
    'fontweight','bold',...
    'string','截止频率',...
    'units','normalized',...
    'position',[0.1 0.15 0.1 0.04]);
F.edit_freq = uicontrol(F.figure_main,'tag','edit_freq',...
    'style','edit',...
    'fontsize',12,...
    'units','normalized',...
    'callback',@(hObject,eventdata)opt_idealHPfreqfilter_freq_Callback(hObject,eventdata),...
    'position',[0.22 0.15 0.1 0.05]);
align([text_F_freq,F.edit_freq],'none','center');
set(F.edit_freq,'string','100');
setappdata(F.figure_main,'freq',100);
opt_idealHPfreqfilter_freq_Callback([],[]);



function opt_idealHPfreqfilter_freq_Callback(~,~)
%% 理想高通滤波
global F;
im = getappdata(F.figure_main,'image');
try
    freq = str2double(get(F.edit_freq,'string'));
    assert(isreal(freq) && freq>=0);
catch
    set(F.edit_freq,'string',num2str(getappdata(F.figure_main,'freq')));
    return;
end
turnBusy;
try
    imtemp = x_imfreqfilter(im,'idealH',freq);    % 理想高通滤波
catch
    uiwait(errordlg('操作失败。','错误','modal'));
    set(F.edit_freq,'string',num2str(getappdata(F.figure_main,'freq')));
    turnFree;
    return;
end
turnFree;
set(F.edit_freq,'string',num2str(freq));
setappdata(F.figure_main,'freq',freq);
axes(F.axes_image);
I = imshow(imtemp);
set(F.text_imsize,'string',[num2str(size(imtemp,1)) '×' num2str(size(imtemp,2)) '×' num2str(size(imtemp,3))]);
set(I,'uicontextmenu',F.uicontextmenu_main);
setappdata(F.figure_main,'imtemp',imtemp);



function uimenu_butterworthLPfreqfilter_Callback(~,~)
%% 布特沃斯低通滤波菜单回调
showChildFigure([0.1 0.3 0.8 0.6],'布特沃斯低通滤波');
global F;
text_F_freq= uicontrol(F.figure_main,'tag','text_F_freq',...
    'style','text',...
    'fontsize',12,...
    'fontweight','bold',...
    'string','截止频率',...
    'units','normalized',...
    'position',[0.1 0.15 0.1 0.04]);
F.edit_freq = uicontrol(F.figure_main,'tag','edit_freq',...
    'style','edit',...
    'fontsize',12,...
    'units','normalized',...
    'callback',@(hObject,eventdata)opt_butterworthLPfreqfilter_power_Callback(hObject,eventdata),...
    'position',[0.22 0.15 0.1 0.05]);
text_F_power = uicontrol(F.figure_main,'tag','text_F_power',...
    'style','text',...
    'fontsize',12,...
    'fontweight','bold',...
    'string','阶数',...
    'units','normalized',...
    'position',[0.37 0.15 0.05 0.04]);
F.edit_power = uicontrol(F.figure_main,'tag','edit_power',...
    'style','edit',...
    'fontsize',12,...
    'units','normalized',...
    'callback',@(hObject,eventdata)opt_butterworthLPfreqfilter_power_Callback(hObject,eventdata),...
    'position',[0.44 0.15 0.1 0.05]);
align([text_F_freq,text_F_power,F.edit_freq,F.edit_power],'none','center');
set(F.edit_freq,'string','100');
setappdata(F.figure_main,'freq',100);
set(F.edit_power,'string','1');
setappdata(F.figure_main,'power',1);
opt_butterworthLPfreqfilter_power_Callback([],[]);



function opt_butterworthLPfreqfilter_power_Callback(~,~)
%% 布特沃斯低通滤波
global F;
im = getappdata(F.figure_main,'image');
try
    freq = str2double(get(F.edit_freq,'string'));
    assert(isreal(freq) && freq>=0);
    power = str2double(get(F.edit_power,'string'));
    assert(isreal(power) && power>=1 && isequal(power,round(power)));
catch
    set(F.edit_freq,'string',num2str(getappdata(F.figure_main,'freq')));
    set(F.edit_power,'string',num2str(getappdata(F.figure_main,'power')));
    return;
end
turnBusy;
try
    imtemp = x_imfreqfilter(im,'butterworthL',freq,power);    % 布特沃斯低通滤波
catch
    uiwait(errordlg('操作失败。','错误','modal'));
    set(F.edit_freq,'string',num2str(freq));
    set(F.edit_power,'string',num2str(getappdata(F.figure_main,'power')));
    turnFree;
    return;
end
turnFree;
set(F.edit_freq,'string',num2str(freq));
set(F.edit_power,'string',num2str(power));
setappdata(F.figure_main,'freq',freq);
setappdata(F.figure_main,'power',power);
axes(F.axes_image);
I = imshow(imtemp);
set(F.text_imsize,'string',[num2str(size(imtemp,1)) '×' num2str(size(imtemp,2)) '×' num2str(size(imtemp,3))]);
set(I,'uicontextmenu',F.uicontextmenu_main);
setappdata(F.figure_main,'imtemp',imtemp);




function uimenu_butterworthHPfreqfilter_Callback(~,~)
%% 布特沃斯高通滤波菜单回调
showChildFigure([0.1 0.3 0.8 0.6],'布特沃斯高通滤波');
global F;
text_F_freq= uicontrol(F.figure_main,'tag','text_F_freq',...
    'style','text',...
    'fontsize',12,...
    'fontweight','bold',...
    'string','截止频率',...
    'units','normalized',...
    'position',[0.1 0.15 0.1 0.04]);
F.edit_freq = uicontrol(F.figure_main,'tag','edit_freq',...
    'style','edit',...
    'fontsize',12,...
    'units','normalized',...
    'callback',@(hObject,eventdata)opt_butterworthHPfreqfilter_power_Callback(hObject,eventdata),...
    'position',[0.22 0.15 0.1 0.05]);
text_F_power = uicontrol(F.figure_main,'tag','text_F_power',...
    'style','text',...
    'fontsize',12,...
    'fontweight','bold',...
    'string','阶数',...
    'units','normalized',...
    'position',[0.37 0.15 0.05 0.04]);
F.edit_power = uicontrol(F.figure_main,'tag','edit_power',...
    'style','edit',...
    'fontsize',12,...
    'units','normalized',...
    'callback',@(hObject,eventdata)opt_butterworthHPfreqfilter_power_Callback(hObject,eventdata),...
    'position',[0.44 0.15 0.1 0.05]);
align([text_F_freq,text_F_power,F.edit_freq,F.edit_power],'none','center');
set(F.edit_freq,'string','100');
setappdata(F.figure_main,'freq',100);
set(F.edit_power,'string','1');
setappdata(F.figure_main,'power',1);
opt_butterworthHPfreqfilter_power_Callback([],[]);



function opt_butterworthHPfreqfilter_power_Callback(~,~)
%% 布特沃斯高通滤波
global F;
im = getappdata(F.figure_main,'image');
try
    freq = str2double(get(F.edit_freq,'string'));
    assert(isreal(freq) && freq>=0);
    power = str2double(get(F.edit_power,'string'));
    assert(isreal(power) && power>=1 && isequal(power,round(power)));
catch
    set(F.edit_freq,'string',num2str(getappdata(F.figure_main,'freq')));
    set(F.edit_power,'string',num2str(getappdata(F.figure_main,'power')));
    return;
end
turnBusy;
try
    imtemp = x_imfreqfilter(im,'butterworthH',freq,power);    % 布特沃斯高通滤波
catch
    uiwait(errordlg('操作失败。','错误','modal'));
    set(F.edit_freq,'string',num2str(freq));
    set(F.edit_power,'string',num2str(getappdata(F.figure_main,'power')));
    turnFree;
    return;
end
turnFree;
set(F.edit_freq,'string',num2str(freq));
set(F.edit_power,'string',num2str(power));
setappdata(F.figure_main,'freq',freq);
setappdata(F.figure_main,'power',power);
axes(F.axes_image);
I = imshow(imtemp);
set(F.text_imsize,'string',[num2str(size(imtemp,1)) '×' num2str(size(imtemp,2)) '×' num2str(size(imtemp,3))]);
set(I,'uicontextmenu',F.uicontextmenu_main);
setappdata(F.figure_main,'imtemp',imtemp);



function uimenu_gaussianLPfreqfilter_Callback(~,~)
%% 高斯低通滤波菜单回调
showChildFigure([0.1 0.3 0.8 0.6],'高斯低通滤波');
global F;
text_F_freq = uicontrol(F.figure_main,'tag','text_F_freq',...
    'style','text',...
    'fontsize',12,...
    'fontweight','bold',...
    'string','截止频率',...
    'units','normalized',...
    'position',[0.1 0.15 0.1 0.04]);
F.edit_freq = uicontrol(F.figure_main,'tag','edit_freq',...
    'style','edit',...
    'fontsize',12,...
    'units','normalized',...
    'callback',@(hObject,eventdata)opt_gaussianLPfreqfilter_freq_Callback(hObject,eventdata),...
    'position',[0.22 0.15 0.1 0.05]);
align([text_F_freq,F.edit_freq],'none','center');
set(F.edit_freq,'string','100');
setappdata(F.figure_main,'freq',100);
opt_gaussianLPfreqfilter_freq_Callback([],[]);



function opt_gaussianLPfreqfilter_freq_Callback(~,~)
%% 高斯低通滤波
global F;
im = getappdata(F.figure_main,'image');
try
    freq = str2double(get(F.edit_freq,'string'));
    assert(isreal(freq) && freq>=0);
catch
    set(F.edit_freq,'string',num2str(getappdata(F.figure_main,'freq')));
    return;
end
turnBusy;
try
    imtemp = x_imfreqfilter(im,'gaussianL',freq);    % 高斯低通滤波
catch
    uiwait(errordlg('操作失败。','错误','modal'));
    set(F.edit_freq,'string',num2str(getappdata(F.figure_main,'freq')));
    turnFree;
    return;
end
turnFree;
set(F.edit_freq,'string',num2str(freq));
setappdata(F.figure_main,'freq',freq);
axes(F.axes_image);
I = imshow(imtemp);
set(F.text_imsize,'string',[num2str(size(imtemp,1)) '×' num2str(size(imtemp,2)) '×' num2str(size(imtemp,3))]);
set(I,'uicontextmenu',F.uicontextmenu_main);
setappdata(F.figure_main,'imtemp',imtemp);




function uimenu_gaussianHPfreqfilter_Callback(~,~)
%% 高斯高通滤波菜单回调
showChildFigure([0.1 0.3 0.8 0.6],'高斯高通滤波');
global F;
text_F_freq = uicontrol(F.figure_main,'tag','text_F_freq',...
    'style','text',...
    'fontsize',12,...
    'fontweight','bold',...
    'string','截止频率',...
    'units','normalized',...
    'position',[0.1 0.15 0.1 0.04]);
F.edit_freq = uicontrol(F.figure_main,'tag','edit_freq',...
    'style','edit',...
    'fontsize',12,...
    'units','normalized',...
    'callback',@(hObject,eventdata)opt_gaussianHPfreqfilter_freq_Callback(hObject,eventdata),...
    'position',[0.22 0.15 0.1 0.05]);
align([text_F_freq,F.edit_freq],'none','center');
set(F.edit_freq,'string','100');
setappdata(F.figure_main,'freq',100);
opt_gaussianHPfreqfilter_freq_Callback([],[]);



function opt_gaussianHPfreqfilter_freq_Callback(~,~)
%% 高斯高通滤波
global F;
im = getappdata(F.figure_main,'image');
try
    freq = str2double(get(F.edit_freq,'string'));
    assert(isreal(freq) && freq>=0);
catch
    set(F.edit_freq,'string',num2str(getappdata(F.figure_main,'freq')));
    return;
end
turnBusy;
try
    imtemp = x_imfreqfilter(im,'gaussianH',freq);    % 高斯高通滤波
catch
    uiwait(errordlg('操作失败。','错误','modal'));
    set(F.edit_freq,'string',num2str(getappdata(F.figure_main,'freq')));
    turnFree;
    return;
end
turnFree;
set(F.edit_freq,'string',num2str(freq));
setappdata(F.figure_main,'freq',freq);
axes(F.axes_image);
I = imshow(imtemp);
set(F.text_imsize,'string',[num2str(size(imtemp,1)) '×' num2str(size(imtemp,2)) '×' num2str(size(imtemp,3))]);
set(I,'uicontextmenu',F.uicontextmenu_main);
setappdata(F.figure_main,'imtemp',imtemp);



function uimenu_noise_Callback(~,~)
%% 噪声菜单回调
global handles;
tab = get(handles.uitabgroup_main,'selectedtab');
canOperate = {'off','off'};
if ~isempty(tab)
    im = getappdata(tab,'image');
    if ~islogical(im)
        canOperate = {'on','on'};
    end
end
set(handles.uimenu_estnoise,'enable',canOperate{1});
set(handles.uimenu_addnoise,'enable',canOperate{2});




function uimenu_estnoise_Callback(~,~)
%% 噪声估计菜单回调
try
    im = imcrop();    % 选择区域
    assert(~isempty(im));
catch
    return;
end
figure_main = figure('tag','figure_F_main',...
    'name','噪声估计',...    % 窗口标题
    'numbertitle','off',...    % 数字标题关闭
    'menubar','none',...    % 去除菜单栏
    'toolbar','none',...    % 显示工具栏
    'resize','on',...    % 窗口大小可变
    'windowstyle','modal',...
    'units','normalized',...
    'position',[0.25 0.2 0.5 0.5]);    % 窗口位置及大小
axes_image = axes(figure_main,'tag','axes_image',...
    'visible','off',...
    'units','normalized',...
    'position',[0.1 0.2 0.35 0.7]);
axes(axes_image);
imshow(im);
axes_hist = axes(figure_main,'tag','axes_hist',...
    'visible','off',...
    'units','normalized',...
    'position',[0.55 0.2 0.35 0.7]);
axes(axes_hist);
histogram(im);
text_imsize = uicontrol(figure_main,'tag','text_imsize',...
    'style','text',...
    'fontsize',12,...
    'HorizontalAlignment','left',...
    'string',[],...
    'units','normalized',...
    'position',[0.01 0 0.2 0.05]);
set(text_imsize,'string',[num2str(size(im,1)) '×' num2str(size(im,2)) '×' num2str(size(im,3))]);
meanval = mean(double(im(:)));
uicontrol(figure_main,'tag','text_meanval',...
    'style','text',...
    'fontsize',12,...
    'string',['均值  ' num2str(meanval)],...
    'units','normalized',...
    'position',[0.1 0.1 0.2 0.05]);
varval = std(double(im(:)))^2;
uicontrol(figure_main,'tag','text_varval',...
    'style','text',...
    'fontsize',12,...
    'string',['方差  ' num2str(varval)],...
    'units','normalized',...
    'position',[0.35 0.1 0.2 0.05]);



function uimenu_gausnoise_Callback(~,~)
%% 高斯噪声菜单回调
showChildFigure([0.1 0.3 0.8 0.6],'高斯噪声');
global F;
text_F_meanval= uicontrol(F.figure_main,'tag','text_F_meanval',...
    'style','text',...
    'fontsize',12,...
    'fontweight','bold',...
    'string','均值',...
    'units','normalized',...
    'position',[0.1 0.15 0.05 0.04]);
F.edit_meanval = uicontrol(F.figure_main,'tag','edit_meanval',...
    'style','edit',...
    'fontsize',12,...
    'units','normalized',...
    'callback',@(hObject,eventdata)opt_gausnoise_varval_Callback(hObject,eventdata),...
    'position',[0.17 0.15 0.1 0.05]);
text_F_varval = uicontrol(F.figure_main,'tag','text_F_varval',...
    'style','text',...
    'fontsize',12,...
    'fontweight','bold',...
    'string','方差',...
    'units','normalized',...
    'position',[0.32 0.15 0.05 0.04]);
F.edit_varval = uicontrol(F.figure_main,'tag','edit_varval',...
    'style','edit',...
    'fontsize',12,...
    'units','normalized',...
    'callback',@(hObject,eventdata)opt_gausnoise_varval_Callback(hObject,eventdata),...
    'position',[0.39 0.15 0.1 0.05]);
align([text_F_meanval,text_F_varval,F.edit_meanval,F.edit_varval],'none','center');
set(F.edit_meanval,'string','0');
setappdata(F.figure_main,'meanval',0);
set(F.edit_varval,'string','0');
setappdata(F.figure_main,'varval',0);
opt_gausnoise_varval_Callback([],[]);




function opt_gausnoise_varval_Callback(~,~)
%% 高斯噪声
global F;
im = getappdata(F.figure_main,'image');
try
    meanval = str2double(get(F.edit_meanval,'string'));
    assert(isreal(meanval));
    varval = str2double(get(F.edit_varval,'string'));
    assert(isreal(varval) && varval>=0);
catch
    set(F.edit_meanval,'string',num2str(getappdata(F.figure_main,'meanval')));
    set(F.edit_varval,'string',num2str(getappdata(F.figure_main,'varval')));
    return;
end
turnBusy;
try
    imtemp = imnoise(im,'gaussian',meanval,varval);    % 高斯噪声
catch
    uiwait(errordlg('操作失败。','错误','modal'));
    set(F.edit_meanval,'string',num2str(meanval));
    set(F.edit_varval,'string',num2str(getappdata(F.figure_main,'varval')));
    turnFree;
    return;
end
turnFree;
set(F.edit_meanval,'string',num2str(meanval));
set(F.edit_varval,'string',num2str(varval));
setappdata(F.figure_main,'meanval',meanval);
setappdata(F.figure_main,'varval',varval);
axes(F.axes_image);
I = imshow(imtemp);
set(F.text_imsize,'string',[num2str(size(imtemp,1)) '×' num2str(size(imtemp,2)) '×' num2str(size(imtemp,3))]);
set(I,'uicontextmenu',F.uicontextmenu_main);
setappdata(F.figure_main,'imtemp',imtemp);




function uimenu_spnoise_Callback(~,~)
%% 椒盐噪声菜单回调
showChildFigure([0.1 0.3 0.8 0.6],'高斯高通滤波');
global F;
text_F_den = uicontrol(F.figure_main,'tag','text_F_den',...
    'style','text',...
    'fontsize',12,...
    'fontweight','bold',...
    'string','密度',...
    'units','normalized',...
    'position',[0.1 0.15 0.05 0.04]);
F.edit_den = uicontrol(F.figure_main,'tag','edit_den',...
    'style','edit',...
    'fontsize',12,...
    'units','normalized',...
    'callback',@(hObject,eventdata)opt_spnoise_den_Callback(hObject,eventdata),...
    'position',[0.17 0.15 0.1 0.05]);
align([text_F_den,F.edit_den],'none','center');
set(F.edit_den,'string','0');
setappdata(F.figure_main,'den',0);
opt_spnoise_den_Callback([],[]);



function opt_spnoise_den_Callback(~,~)
%% 椒盐噪声
global F;
im = getappdata(F.figure_main,'image');
try
    den = str2double(get(F.edit_den,'string'));
    assert(isreal(den) && den>=0 && den<=1);
catch
    set(F.edit_den,'string',num2str(getappdata(F.figure_main,'den')));
    return;
end
turnBusy;
try
    imtemp = imnoise(im,'salt & pepper',den);    % 椒盐噪声
catch
    uiwait(errordlg('操作失败。','错误','modal'));
    set(F.edit_den,'string',num2str(getappdata(F.figure_main,'den')));
    turnFree;
    return;
end
turnFree;
set(F.edit_den,'string',num2str(den));
setappdata(F.figure_main,'den',den);
axes(F.axes_image);
I = imshow(imtemp);
set(F.text_imsize,'string',[num2str(size(imtemp,1)) '×' num2str(size(imtemp,2)) '×' num2str(size(imtemp,3))]);
set(I,'uicontextmenu',F.uicontextmenu_main);
setappdata(F.figure_main,'imtemp',imtemp);



function showChildFigure(axesPosition,title)
%% 显示子窗口
global handles;
global F;
F = struct;
tab = get(handles.uitabgroup_main,'selectedtab');    % 获取当前选项卡
im = getappdata(tab,'image');
F.figure_main = figure('tag','figure_F_main',...
    'name',title,...    % 窗口标题
    'numbertitle','off',...    % 数字标题关闭
    'menubar','none',...    % 去除菜单栏
    'toolbar','none',...    % 显示工具栏
    'resize','on',...    % 窗口大小可变
    'closerequestfcn','global F;F.deleted = true;delete(F.figure_main);',...
    'windowstyle','modal',...    % 默认窗口风格
    'units','normalized',...
    'position',[0.25 0.2 0.5 0.5]);    % 窗口位置及大小
F.axes_image = axes(F.figure_main,'tag','axes_image',...
    'visible','off',...
    'units','normalized',...
    'position',axesPosition);
axes(F.axes_image);
I = imshow(im);
F.text_imsize = uicontrol(F.figure_main,'tag','text_imsize',...
    'style','text',...
    'fontsize',12,...
    'HorizontalAlignment','left',...
    'string',[],...
    'units','normalized',...
    'position',[0.01 0 0.2 0.05]);
set(F.text_imsize,'string',[num2str(size(im,1)) '×' num2str(size(im,2)) '×' num2str(size(im,3))]);
F.uicontextmenu_main = uicontextmenu('tag','uicontextmenu_F');
set(I,'uicontextmenu',F.uicontextmenu_main);    % 设置选项卡快捷菜单
uimenu(F.uicontextmenu_main,'tag','uimenu_F_pan',...
    'label','平移',...
    'callback','global F;H = pan(F.figure_main);set(H,''uicontextmenu'',F.uicontextmenu_main);H.Enable = ''on'';clear F;clear H;');
uimenu(F.uicontextmenu_main,'tag','uimenu_F_zoom',...
    'label','缩放',...
    'callback','global F;H = zoom(F.figure_main);set(H,''uicontextmenu'',F.uicontextmenu_main);H.Enable = ''on'';clear F;clear H;');
uicontrol(F.figure_main,'tag','pushbutton_F_ok',...
    'style','pushbutton',...
    'fontsize',12,...
    'fontweight','bold',...
    'busyaction','cancel',...
    'string','确认',...
    'units','normalized',...
    'callback',@(hObject,eventdata)pushbutton_F_ok_Callback(hObject,eventdata),...
    'position',[0.8 0.05 0.08 0.08]);
uicontrol(F.figure_main,'tag','pushbutton_F_cancel',...
    'style','pushbutton',...
    'fontsize',12,...
    'fontweight','bold',...
    'foregroundcolor',[1.0 0 0],...
    'string','取消',...
    'units','normalized',...
    'callback','global F;F.deleted = true;delete(F.figure_main);',...
    'position',[0.9 0.05 0.08 0.08]);
setappdata(F.figure_main,'image',im);
setappdata(F.figure_main,'imtemp',im);


function pushbutton_F_ok_Callback(~,~)
%% 子窗口确认按钮回调
global F;
imtemp = getappdata(F.figure_main,'imtemp');
if ~isequal(imtemp,getappdata(F.figure_main,'image'))
    delete(F.figure_main);
    forward(imtemp);
else
    delete(F.figure_main);
end




function turnBusy
%% 正忙
global handles;
global F;
try
    figure_main = F.figure_main;
    assert(ishandle(figure_main));
    P = get(figure_main,'position');
catch
    figure_main = handles.figure_main;
    P = get(figure_main,'position');
end
P = [P(1) + P(3) / 2 - 0.075,P(2) + P(4) / 2 - 0.05,0.15,0.1];
F.busyFigure = figure('tag','figure_busy',...
    'name','正忙',...    % 窗口标题
    'numbertitle','off',...    % 数字标题关闭
    'menubar','none',...    % 去除菜单栏
    'toolbar','none',...    % 显示工具栏
    'resize','off',...    % 窗口大小可变
    'windowstyle','modal',...    % 窗口风格
    'closerequestfcn','global F;if ~getappdata(F.busyFigure,''busy''),delete(F.busyFigure),end',...    % 窗口将要关闭时回调
    'units','normalized',...
    'position',P);    % 窗口位置及大小
uicontrol(F.busyFigure,'tag','text_busyF',...
    'style','text',...
    'fontsize',10,...
    'fontweight','bold',...
    'string','正在计算，请稍候...',...
    'units','normalized',...
    'position',[0 0.4 1.0 0.3]);
drawnow;
setappdata(F.busyFigure,'busy',true);


function turnFree
%% 解除正忙状态
global F;
setappdata(F.busyFigure,'busy',false);
delete(F.busyFigure);




function uicontextmenu_tab_Callback(~,~)
%% 选项卡快捷菜单回调
global handles;
tab = get(handles.uitabgroup_main,'selectedtab');    % 获取当前选项卡
% 设置关闭其他选项卡菜单是否可用
if length(allchild(handles.uitabgroup_main)) == 1
    set(getappdata(tab,'uimenu_closeothers'),'enable','off');
else
    set(getappdata(tab,'uimenu_closeothers'),'enable','on');
end



function closed = uitab_close_Callback(~,~)
%% 关闭选项卡
global handles;
closed = false;
tab = get(handles.uitabgroup_main,'selectedtab');    % 获取当前选项卡
if shouldImSave    % 图像应当保存
    choice = questdlg(['是否保存对图像' getappdata(tab,'imageName') '的修改？'],'询问','保存','不保存','取消','保存');
    if strcmp(choice,'保存')
        if ~file_save_Callback([],[])    % 保存图像失败
            return;
        end
    elseif isempty(choice) || strcmp(choice,'取消')    % 取消
        return;
    end
end
% 不需要保存图像，或保存图像成功，或不保存图像
try
    fclose(getappdata(tab,'fileID'));    % 关闭文件流
catch
end
delete(tab);    % 删除选项卡
uimenu_edit_Callback([],[]);
if isempty(allchild(handles.uitabgroup_main))
    file_Callback([],[]);    % 文件菜单回调
end
closed = true;



function closed = uitab_closeothers_Callback(~,~)
%% 关闭其他选项卡
global handles;
closed = true;
tab = get(handles.uitabgroup_main,'selectedtab');    % 获取当前选项卡
tabs = allchild(handles.uitabgroup_main);    % 所有选项卡
for i = 1:length(tabs)
    if tabs(i) == tab    % 跳过当前选项卡
        continue;
    end
    set(handles.uitabgroup_main,'selectedtab',tabs(i));
    if ~uitab_close_Callback([],[])    % 关闭选项卡失败
        closed = false;
    end
end



function closed = uitab_closeall_Callback(~,~)
%% 关闭所有选项卡
global handles;
closed = true;
tabs = allchild(handles.uitabgroup_main);    % 所有选项卡
for i = 1:length(tabs)
    set(handles.uitabgroup_main,'selectedtab',tabs(i));
    if ~uitab_close_Callback([],[])    % 关闭选项卡失败
        closed = false;
    end
end


function forward(im)
%% 修改
global handles;
tab = get(handles.uitabgroup_main,'selectedtab');
axes(getappdata(tab,'axes_image'));
imshow(im);
set(getappdata(tab,'text_imsize'),'string',[num2str(size(im,1)) '×' num2str(size(im,2)) '×' num2str(size(im,3))]);
imageStackA = getappdata(tab,'imageStackA');
imageStackA{length(imageStackA) + 1} = im;
setappdata(tab,'image',im);
setappdata(tab,'imageStackA',imageStackA);
setappdata(tab,'imageStackB',{});
shouldImSave;
uimenu_edit_Callback([],[]);



function redo_Callback(~,~)
%% 恢复
global handles;
tab = get(handles.uitabgroup_main,'selectedtab');
imageStackA = getappdata(tab,'imageStackA');
imageStackB = getappdata(tab,'imageStackB');
im = imageStackB{length(imageStackB)};
imageStackB(length(imageStackB)) = [];
imageStackA{length(imageStackA) + 1} = im;
axes(getappdata(tab,'axes_image'));
imshow(im);
set(getappdata(tab,'text_imsize'),'string',[num2str(size(im,1)) '×' num2str(size(im,2)) '×' num2str(size(im,3))]);
setappdata(tab,'image',im);
setappdata(tab,'imageStackA',imageStackA);
setappdata(tab,'imageStackB',imageStackB);
shouldImSave;
uimenu_edit_Callback([],[]);


function undo_Callback(~,~)
%% 撤销
global handles;
tab = get(handles.uitabgroup_main,'selectedtab');    % 获取当前选项卡
imageStackA = getappdata(tab,'imageStackA');
imageStackB = getappdata(tab,'imageStackB');
imageStackB{length(imageStackB) + 1} = imageStackA{length(imageStackA)};
imageStackA(length(imageStackA)) = [];
im = imageStackA{length(imageStackA)};
axes(getappdata(tab,'axes_image'));
imshow(im);
set(getappdata(tab,'text_imsize'),'string',[num2str(size(im,1)) '×' num2str(size(im,2)) '×' num2str(size(im,3))]);
setappdata(tab,'image',im);
setappdata(tab,'imageStackA',imageStackA);
setappdata(tab,'imageStackB',imageStackB);
shouldImSave;
uimenu_edit_Callback([],[]);



function result = shouldImSave
%% 图像是否应该保存
global handles;
tab = get(handles.uitabgroup_main,'selectedtab');    % 获取当前选项卡
imageName = getappdata(tab,'imageName');    % 图像名称
im = getappdata(tab,'image');    % 图像
if isequal(im,getappdata(tab,'originalImage'))    % 图像未被修改，重置菜单不可用
    set(handles.uimenu_edit_reset,'enable','off');
else    % 图像被修改，重置菜单可用
    set(handles.uimenu_edit_reset,'enable','on');
end
if ~isequal(im,getappdata(tab,'originalImage')) ||...    % 图像被修改
        ftell(getappdata(tab,'fileID')) == -1 || ...    % 图像文件被非法修改
        ~exist([getappdata(tab,'pathName') imageName],'file')    % 图像文件被删除
    set(tab,'title',[imageName ' *']);    % 图像应该保存
    result = true;
else
    set(tab,'title',imageName);    % 图像不需要保存
    result = false;
end