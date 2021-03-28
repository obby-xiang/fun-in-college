% @since 2017-12-26
function B = x_imgammatransf(A,GAMMA)
%% 图像幂律（伽马）变换
% 函数说明:
%     B = x_imgammatransf(A,GAMMA)
%     A:图像(灰度图)    GAMMA:伽马值   B:伽马变换后的图像
% 原理或算法:
%     s是变换后图像的像素值,r是原图的像素值,则s=c*r^gamma (c=1)
% 示例:
%     I = imread('fractured_spine.tif');
%     M = imread('washed_out_aerial_image.tif');
%     J = x_imgammatransf(double(I),0.4);
%     N = x_imgammatransf(double(M),4);
%     figure,subplot(2,2,1),imshow(I);
%     subplot(2,2,2),imshow(J,[]),title(['\gamma=' num2str(0.4)]);
%     subplot(2,2,3),imshow(M);
%     subplot(2,2,4),imshow(N,[]),title(['\gamma=' num2str(4)]);
assert(isnumeric(A) && isreal(A) && ismatrix(A),'图像参数有误,必须是灰度图且像素值是实数');
assert(isnumeric(GAMMA) && isreal(GAMMA) && numel(GAMMA) == 1,'伽马参数有误,必须是实数');
B = cast(double(A).^double(GAMMA),'like',A);