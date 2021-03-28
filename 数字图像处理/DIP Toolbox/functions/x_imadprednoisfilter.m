% @since 2017-12-26
function B = x_imadprednoisfilter(A,WINSIZE,NOISVAR)
%% 自适应局部降低噪声滤波器
% 函数说明:
%     B = x_imadprednoisfilter(A,WINSIZE,NOISVAR)
%     A:图像(灰度图或彩色图)    WINSIZE:每次处理的窗口大小    NOISVAR:全局噪声方差
%     B:自适应局部降低噪声滤波后的图像
% 原理或算法:
%     输入图像f(x,y),图像内邻域Sxy,输出图像g(x,y),全局噪声方差V,邻域方差Vl,邻域均值Ml
%     于是有g(x,y)=f(x,y)-V/Vl*(f(x,y)-Ml)
% 示例:
%     I = imread('ckt_gaussian_var_1000_mean_0.tif');
%     J = x_imadprednoisfilter(I,7,1000);
%     figure,subplot(1,2,1),imshow(I);
%     subplot(1,2,2),imshow(J);
assert(isnumeric(A) && isreal(A) && (ismatrix(A) || ndims(A)==3),...
    '图像参数有误,必须是灰度图或彩色图且像素值是实数');
assert(isnumeric(WINSIZE) && isreal(WINSIZE) &&...
    (numel(WINSIZE)==1 || numel(WINSIZE)==2) &&...
    sum(WINSIZE>0)==length(WINSIZE) && isequal(WINSIZE,round(WINSIZE)),...
    '窗口大小参数有误,必须是整数或长度为2的整数(行或列)向量');
assert(isnumeric(NOISVAR) && isreal(NOISVAR) && numel(NOISVAR)==1,...
    '全部噪声方差参数有误,必须是实数');
if numel(WINSIZE) == 1
    WINSIZE = [WINSIZE WINSIZE];
end
WINSIZE = double(WINSIZE);
NOISVAR = double(NOISVAR);
[m,n,r] = size(A);    % 输入图像大小
B = cast(zeros(m,n,r),'like',A);    % 初始化输出图像
p = WINSIZE(1);
q = WINSIZE(2);

% 扩充图像
A = [A;zeros(p,size(A,2),r)];
A = [A,zeros(size(A,1),p,r)];

A = double(A);
NOISVAR = double(NOISVAR);
for i = 1:m
    for j = 1:n
        for k = 1:r
            temp = A(i:i + p - 1,j:j + q - 1,k);
            temp = temp(:);
            V = std(temp)^2;
            if NOISVAR <= V
                B(i,j,k) = A(i,j,k) - NOISVAR/V*(A(i,j,k) - mean(temp));
            else
                B(i,j,k) = mean(temp);
            end
        end
    end
end