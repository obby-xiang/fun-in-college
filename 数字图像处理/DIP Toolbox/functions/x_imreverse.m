% @since 2017-12-26
function B = x_imreverse(A)
%% 图像反转
% 函数说明:
%     B = x_imreverse(A)
%     A:图像(灰度图)   B:反转后的图像
% 原理或算法:
%     s是变换后图像的像素值,r是输入图像的像素值,则s=255-r
% 示例:
%     I = imread('breast_digital_Xray.tif');
%     J = x_imreverse(I);
%     figure,subplot(1,2,1),imshow(I);
%     subplot(1,2,2),imshow(J);
assert(isnumeric(A) && isreal(A) && ismatrix(A),...
    '图像参数有误,必须是灰度图且像素值是实数');
if ~isa(A,'uint8')
    warning(['输入图像的像素值数据类型是' class(A) ',将对其进行归一化处理后转换为uint8类型']);
    A = im2uint8(mat2gray(A));
end
B = cast(255 - double(A),'like',A);