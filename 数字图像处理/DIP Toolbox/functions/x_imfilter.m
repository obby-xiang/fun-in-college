% @since 2017-12-26
function B = x_imfilter(A,TEMPLATE)
%% 空间(卷积)滤波
% 函数说明:
%     B = x_imfilter(A,TEMPLATE)
%     A:图像(灰度图或彩色图)    TEMPLATE:滤波器模板    B:滤波后的图像
% 原理或算法:
%     滤波器为w(x,y),大小为m*n,则g(x,y)=sum(w(0:m-1,0:n-1)*f(x:x+m-1,y+n-1))
% 示例:
%     I = imread('test_pattern_blurring_orig.tif');
%     M = imread('blurry_moon.tif');
%     J = x_imfilter(I,ones(9,9)/81);    % 9×9平滑(均值)滤波器
%     N = M-x_imfilter(M,[1 1 1;1 -8 1;1 1 1]);     % 3×3拉普拉斯
%     figure,subplot(2,2,1),imshow(I);subplot(2,2,2),imshow(J);
%     subplot(2,2,3),imshow(M);subplot(2,2,4),imshow(N);
assert((isnumeric(A) || islogical(A)) && isreal(A) && (ismatrix(A) || ndims(A)==3),...
    '图像参数有误,必须是灰度图或彩色图且像素值是实数');
assert(isnumeric(TEMPLATE) && isreal(TEMPLATE) && ismatrix(TEMPLATE),...
    '模板参数有误,必须是实数矩阵');
[m,n,r]=size(A);    % 输入图像大小
B = cast(zeros(size(A)),'like',A);    % 初始化输出图像
[p,q] = size(TEMPLATE);    % 模板大小

% 扩充图像
if p > 1
    v = round((p - 1) / 2);
    u = p - 1 - v;
    A = [zeros(u,size(A,2),r);A;zeros(v,size(A,2),r)];
end
if q > 1
    v = round((q - 1) / 2);
    u = q - 1 - v;
    A = [zeros(size(A,1),u,r),A,zeros(size(A,1),v,r)];
end

A = double(A);
TEMPLATE = double(TEMPLATE);

% 滤波
for i = 1:m
    for j = 1:n
        for k = 1:r
            temp = A(i:i + p - 1,j:j + q - 1,k).*TEMPLATE;
            B(i,j,k) = sum(temp(:));
        end
    end
end