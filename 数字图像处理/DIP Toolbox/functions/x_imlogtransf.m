% @since 2017-12-26
function B = x_imlogtransf(A)
%% 图像对数变换
% 函数说明:
%     B = x_imlogtransf(A)
%     A:图像(灰度图)   B:对数变换后的图像
% 原理或算法:
%     s是变换后图像的像素值,r是原图的像素值,则s=c*log(1+r) (c=1)
% 示例:
%     I = imread('DFT_no_log.tif');
%     J = x_imlogtransf(double(I));
%     figure,subplot(1,2,1),imshow(I);
%     subplot(1,2,2),imshow(J,[]);
assert(isnumeric(A) && isreal(A) && ismatrix(A),'图像参数有误,必须是灰度图且像素值是实数');
B = cast(log(1 + double(A)),'like',A);