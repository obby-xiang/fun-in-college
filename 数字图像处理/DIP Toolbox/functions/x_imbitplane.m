% @since 2017-12-26
function B = x_imbitplane(A,PLANE)
%% 比特平面分层
% 函数说明:
%     B = x_imbitplane(A,PLANE)
%     A:图像(灰度图)    PLANE:整数1至8   B:比特平面PLANE
% 原理或算法:
%     对图中每一像素,像素值的二进制数(从低到高)第k位是0则置像素值为0,否则置像素值为255
% 示例:
%     I = imread('100-dollars.tif');
%     figure,subplot(3,3,1),imshow(I);
%     for i=1:8
%         J = x_imbitplane(I,i);
%         subplot(3,3,i+1),imshow(J),title(['比特平面' num2str(i)]);
%     end
assert(isnumeric(A) && isreal(A) && ismatrix(A),'图像参数有误,必须是灰度图且像素值是实数');
assert(isnumeric(PLANE) && isreal(PLANE) && numel(PLANE) == 1 &&...
    round(PLANE) == PLANE && PLANE >= 1 && PLANE <= 8,'PLANE参数有误,必须是整数1至8');
PLANE = double(PLANE);
if ~isa(A,'uint8')
    warning(['输入图像的像素值数据类型是' class(A) ',将对其进行归一化处理后转换为uint8类型']);
    A = im2uint8(mat2gray(A));
end
B = uint8(zeros(size(A)));
B(bitget(A,PLANE) == 1) = 255;