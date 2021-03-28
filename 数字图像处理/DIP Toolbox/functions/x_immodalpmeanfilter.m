% @since 2017-12-26
function B = x_immodalpmeanfilter(A,WINSIZE,D)
%% 修正的阿尔法均值滤波
% 函数说明:
%     B = x_immodalpmeanfilter(A,WINSIZE,D)
%     A:图像(灰度图或彩色图)    WINSIZE:每次处理的窗口大小    D:去除窗口内最低灰度值的D/2和最高灰度值的D/2像素
%     B:使用修正的阿尔法均值滤波后的图像
% 原理或算法:
%     除去窗口Sxy中最低灰度值的d/2和最高灰度值的d/2像素,取剩余像素值的均值
% 示例:
%     I= imread('ckt-uniform-plus-saltpepr-prob-pt1.tif');
%     J = x_immodalpmeanfilter(I,5,5);
%     figure,subplot(1,2,1),imshow(I),title('均匀噪声和椒盐噪声污染');
%     subplot(1,2,2),imshow(J),title('5×5滤波器 d=5');
assert(isnumeric(A) && isreal(A) && (ismatrix(A) || ndims(A)==3),...
    '图像参数有误,必须是灰度图或彩色图且像素值是实数');
assert(isnumeric(WINSIZE) && isreal(WINSIZE) &&...
    (numel(WINSIZE)==1 || numel(WINSIZE)==2) &&...
    sum(WINSIZE>0)==length(WINSIZE) && isequal(WINSIZE,round(WINSIZE)),...
    '窗口大小参数有误,必须是整数或长度为2的整数(行或列)向量');
if numel(WINSIZE) == 1
    WINSIZE = [WINSIZE WINSIZE];
end
WINSIZE = double(WINSIZE);
assert(isnumeric(D) && isreal(D) && numel(D)==1 && D>=0 && D<WINSIZE(1)*WINSIZE(2) && isequal(round(D),D),...
    ['除去像素个数参数有误,必须是整数0至' num2str(WINSIZE(1) * WINSIZE(2) - 1)]);
D = double(D);
[m,n,r] = size(A);    % 输入图像大小
p = WINSIZE(1);    % 窗口行数
q = WINSIZE(2);    % 窗口列数
B = cast(zeros(m,n,r),'like',A);    % 初始化输出图像

% 扩充输入图像
if p > 1
    v = round((p-1)/2);
    u = p - 1 - v;
    A = [zeros(u,size(A,2),r);A;zeros(v,size(A,2),r)];
end
if q > 1
    v = round((q - 1) / 2);
    u = q - 1 - v;
    A = [zeros(size(A,1),u,r),A,zeros(size(A,1),v,r)];
end

numtemp = p * q;    % 窗口容纳像素的数量
A = double(A);
v = round(D / 2);    % 除去高灰度值的个数
u = D - v;    % 除去低灰度值的个数
for i = 1:m
    for j = 1:n
        for k = 1:r
            temp = A(i:i + p - 1,j:j + q - 1,k);
            temp = sort(temp(:));
            B(i,j,k) = mean(temp(u + 1:numtemp - v));
        end
    end
end