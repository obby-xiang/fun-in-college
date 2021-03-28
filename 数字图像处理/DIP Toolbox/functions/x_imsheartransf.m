% @since 2017-12-26
function B = x_imsheartransf(A,SHX,SHY,METHOD)
%% 图像剪切变换
% 函数说明:
%     B = x_imsheartransf(A,SHX,SHY,METHOD)
%     A:图像(灰度图或彩色图)    SHX:水平偏移    SHY:竖直偏移
%     METHOD:像素处理方式('nearest':最近邻插值 'bilinear':双线性插值),默认是'bilinear'
%     B:剪切变换后的图像
% 原理或算法:
%     剪切变换矩阵:[1 shx 0;shy 1 0;0 0 1]
%     具体操作见源码
% 示例:
%     I = imread('lena_gray_512.tif');
%     M = imread('lena_color_512.tif');
%     J = x_imsheartransf(I,0.2,0.6);
%     N=x_imsheartransf(M,-0.6,-0.2);
%     subplot(2,2,1),imshow(I),subplot(2,2,2),imshow(J);
%     subplot(2,2,3),imshow(M),subplot(2,2,4),imshow(N);
assert((isnumeric(A) || islogical(A)) && isreal(A) && (ismatrix(A) || ndims(A)==3),...
    '图像参数有误,必须是灰度图或彩色图且像素值是实数');
assert(isnumeric(SHX) && isreal(SHX) && numel(SHX)==1,...
    '水平偏移参数有误,必须是实数');
assert(isnumeric(SHY) && isreal(SHY) && numel(SHY)==1,...
    '竖直偏移参数有误,必须是实数');
if nargin == 4
    assert(strcmp(METHOD,'nearest') || strcmp(METHOD,'bilinear'),...
        '像素处理方式参数有误,必须是 ''nearest'' 或 ''bilinear''');
else
    METHOD = 'bilinear';
end
m = size(A,1);    % 输入图像行数
n = size(A,2);    % 输入图像列数
SHX = double(SHX);
SHY = double(SHY);
sheartransf = [1,SHX,0;SHY,1,0;0,0,1];    % 剪切变换矩阵
temp = [1,1,1;1,n,1;m,1,1;m,n,1] * sheartransf;    % 剪切后图像四个角的顶点坐标
p = round(max(temp(:,1)) - min(temp(:,1))) + 1;    % 输出图像行数
q = round(max(temp(:,2)) - min(temp(:,2))) + 1;    % 输出图像列数
transltransf = [1,0,0;0,1,0;1 - min(temp(:,1)),1 - min(temp(:,2)),1];    % 规范剪切后图像的坐标(平移剪切后的图像使得最小的行坐标和最小的列坐标均为1)
transf = sheartransf * transltransf;    % 仿射矩阵
if strcmp(METHOD,'nearest')    % 最近邻插值
    B = im_affinetransf_nearest(A,transf,p,q);
elseif strcmp(METHOD,'bilinear')    % 双线性插值
    B = im_affinetransf_bilinear(A,transf,p,q);
end