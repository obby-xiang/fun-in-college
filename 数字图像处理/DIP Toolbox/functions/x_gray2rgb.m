% @since 2017-12-26
function I = x_gray2rgb(A,R,G,B)
%% 灰度图转彩色图
% 函数说明:
%     I = x_gray2rgb(A,R,G,B)
%     A:图像(灰度图)    R:红通道变换函数    G:绿通道变换函数    B:蓝通道变换函数
%     I:灰度图转彩色图后的图像
% 原理或算法:
%     对输入图像执行3个独立的变换,将变换结果分别赋值给输出图像的红、绿、蓝通道
% 示例:
%     I = imread('lena_gray_512.tif');
%     R = abs(sin((1:256) * pi / 128 - pi / 12)) * 255;
%     G = abs(sin((1:256) * pi / 128 - pi / 6)) * 255;
%     B = abs(sin((1:256) * pi / 128 - pi / 3)) * 255;
%     J = x_gray2rgb(I,R,G,B);
%     figure,imshow(J);
assert(isnumeric(A) && isreal(A) && ismatrix(A),'图像参数有误,必须是灰度图且数据类型是实数');
assert(isnumeric(R) && isreal(R) && isvector(R) && length(R)>=256,'红通道变换函数有误,必须是长度不小于256的实数向量');
assert(isnumeric(G) && isreal(G) && isvector(G) && length(G)>=256,'绿通道变换函数有误,必须是长度不小于256的实数向量');
assert(isnumeric(B) && isreal(B) && isvector(B) && length(B)>=256,'蓝通道变换函数有误,必须是长度不小于256的实数向量');
[m,n] = size(A);    % 输入图像大小
if ~isa(A,'uint8')
    warning(['像素值数据类型是' class(A) ',将对其进行归一化处理后转换为uint8类型']);
    A = im2uint8(mat2gray(A));
end
I = uint8(zeros(m,n,3));    % 初始化输出图像

% 灰度值通过各个变换函数映射到各个通道
I(:,:,1) = R(A + 1);
I(:,:,2) = G(A + 1);
I(:,:,3) = B(A + 1);