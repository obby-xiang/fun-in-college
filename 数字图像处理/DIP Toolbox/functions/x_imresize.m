% @since 2017-12-26
function B = x_imresize(A,SCALEX,SCALEY,METHOD)
%% 比例变换
% 函数说明:
%     B = x_imresize(A,SCALEX,SCALEY,METHOD)
%     A:图像(灰度图或彩色图)    SCALEX:竖直比例    SCALEY:水平比例
%     METHOD:像素处理方式('nearest':最近邻插值 'bilinear':双线性插值),默认是'bilinear'
%     B:比例变换后的图像
% 原理或算法:
%     比例变换矩阵:[cx 0 0;0 cy 0;0 0 1]
%     具体操作见源码
% 示例:
%     I = imread('lena_gray_512.tif');
%     M = imread('lena_color_512.tif');
%     J = x_imresize(I,0.6,1);
%     N = x_imresize(M,1.6,1);
%     figure,subplot(2,2,1),imshow(I),subplot(2,2,2),imshow(J);
%     subplot(2,2,3),imshow(M),subplot(2,2,4),imshow(N);
assert((isnumeric(A) || islogical(A)) && isreal(A) && (ismatrix(A) || ndims(A)==3),...
    '图像参数有误,必须是灰度图或彩色图且像素值是实数');
assert(isnumeric(SCALEX) && isreal(SCALEX) && numel(SCALEX)==1,...
    '竖直比例参数有误,必须是实数');
assert(isnumeric(SCALEY) && isreal(SCALEY) && numel(SCALEY)==1,...
    '水平比例参数有误,必须是实数');
if nargin == 4
    assert(strcmp(METHOD,'nearest') || strcmp(METHOD,'bilinear'),...
        '像素处理方式参数有误,必须是 ''nearest'' 或 ''bilinear''');
else
    METHOD = 'bilinear';
end
SCALEX = double(SCALEX);
SCALEY = double(SCALEY);
m = size(A,1);    % 输入图像行数
n = size(A,2);    % 输入图像列数
scaletransf = [SCALEX,0,0;0,SCALEY,0;0,0,1];    % 比例变换
p = round(m * SCALEX);    % 输出图像行数
q = round(n * SCALEY);    % 输出图像列数
if strcmp(METHOD,'nearest')    % 最近邻插值
    B = im_affinetransf_nearest(A,scaletransf,p,q);
elseif strcmp(METHOD,'bilinear')    % 双线性插值
    B = im_affinetransf_bilinear(A,scaletransf,p,q);
end