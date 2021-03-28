% @since 2017-12-26
function B = x_imrotate(varargin)
%% 图像旋转
% 函数说明:
%     B = x_imrotate(A,ANGLE)
%     B = x_imrotate(A,ANGLE,METHOD)
%     B = x_imrotate(A,ANGLE,ROTX,ROTY)
%     B = x_imrotate(A,ANGLE,ROTX,ROTY,METHOD)
%     A:图像(灰度图或彩色图)    ANGLE:旋转角度(角度制),正数时逆时针旋转,负数时逆时针旋转
%     METHOD:像素处理方式('nearest':最近邻插值 'bilinear':双线性插值),默认是'bilinear'
%     ROTX:旋转中心行坐标    ROTY:旋转中心列坐标,含有旋转中心参数时旋转后的图像大小与原图一致(即舍弃超出原图的像素值)
%     B:旋转变换后的图像
% 原理或算法:
%     平移矩阵:[1 0 0;0 1 0;tx ty 1]
%     旋转矩阵:[cos(ANGLE) sin(ANGLE) 0;-sin(ANGLE) cos(ANGLE) 0;0 0 1]
%     具体操作见源码
% 示例:
%     I = imread('lena_gray_512.tif');
%     M = imread('lena_color_512.tif');
%     J = x_imrotate(I,22.5,size(I,1)/2,size(I,2)/2,'nearest');
%     K = x_imrotate(I,22.5,size(I,1)/2,size(I,2)/2);
%     N = x_imrotate(M,22.5,'nearest');
%     L = x_imrotate(M,22.5,'bilinear');
%     figure,subplot(2,3,1),imshow(I);
%     subplot(2,3,2),imshow(J),title('\theta=22.5° 最近邻插值 旋转中心为图像中心');
%     subplot(2,3,3),imshow(K),title('\theta=22.5° 双线性插值 旋转中心为图像中心');
%     subplot(2,3,4),imshow(M);
%     subplot(2,3,5),imshow(N),title('\theta=22.5° 最近邻插值');
%     subplot(2,3,6),imshow(L),title('\theta=22.5° 双线性插值');
assert(nargin==2 || nargin==3 || nargin==4 || nargin==5,'输入参数的个数有误');
A = varargin{1};    % 输入图像
assert((isnumeric(A) || islogical(A)) && isreal(A) && (ismatrix(A) || ndims(A)==3),...
    '图像参数有误,必须是灰度图或彩色图且像素值是实数');
ANGLE = varargin{2};    % 旋转角度
assert(isnumeric(ANGLE) && isreal(ANGLE) && numel(ANGLE)==1,...
    '角度参数有误,必须是实数');
ANGLE = double(ANGLE) / 180 * pi;    % 角度制与弧度制的转换
METHOD = 'bilinear';
if nargin==3 || nargin==5
    METHOD = varargin{nargin};    % 像素处理方式
    assert(strcmp(METHOD,'nearest')||strcmp(METHOD,'bilinear'),...
        '像素处理方式参数有误,必须是 ''nearest'' 或 ''bilinear''');
end
if nargin > 3
    ROTX = varargin{3};    % 旋转中心行坐标
    assert(isnumeric(ROTX) && isreal(ROTX) && numel(ROTX)==1,...
        '旋转中心行坐标参数有误,必须是实数');
    ROTX = double(ROTX);
    ROTY = varargin{4};    % 旋转中心列坐标
    assert(isnumeric(ROTY) && isreal(ROTY) && numel(ROTY)==1,...
        '旋转中心列坐标参数有误,必须是实数');
    ROTY = double(ROTY);
end
m = size(A,1);    % 输入图像行数
n = size(A,2);    % 输入图像列数
rotatransf = [cos(ANGLE),sin(ANGLE),0;-sin(ANGLE),cos(ANGLE),0;0,0,1];    % 旋转变换矩阵
if nargin <= 3 
    temp = [1,1,1;1,n,1;m,1,1;m,n,1] * rotatransf;    % 旋转后图像四个角的顶点坐标
    p = round(max(temp(:,1)) - min(temp(:,1))) + 1;    % 输出图像行数
    q = round(max(temp(:,2)) - min(temp(:,2))) + 1;    % 输出图像列数
    transltransf = [1,0,0;0,1,0;1 - min(temp(:,1)),1 - min(temp(:,2)),1];    % 规范旋转后图像的坐标(平移旋转后的图像使得最小的行坐标和最小的列坐标均为1)
else
    
    % 设置输出图像与输入图像大小一致
    p = m;
    q = n;
    
    transpos = [ROTY,ROTX,1] * rotatransf;    % 旋转中心绕原点旋转后的坐标
    transltransf = [1,0,0;0,1,0;ROTY - transpos(1),ROTX - transpos(2),1];    % 平移旋转后的图像让变换前后规定的旋转中心坐标不变
end
transf = rotatransf * transltransf;    % 仿射矩阵
if strcmp(METHOD,'nearest')    % 最近邻插值
    B = im_affinetransf_nearest(A,transf,p,q);
elseif strcmp(METHOD,'bilinear')    % 双线性插值
    B = im_affinetransf_bilinear(A,transf,p ,q);
end