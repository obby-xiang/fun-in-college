% @since 2017-12-26
function B = x_histloceh(A,NEIBSIZE)
%% 使用直方图统计的图像局部增强
% 函数说明:
%     B = x_histloceh(A,NEIBSIZE)
%     A:图像(灰度图)    NEIBSIZE:邻域大小,不小于1且不大于图像行数和列数的奇数
%     B:使用直方图统计局部增强后的图像
% 原理或算法:
%     图像全局均值 mG,全局标准差 sigmaG,对每一邻域,邻域中心(x,y),局部均值 mS,局部方差 sigmaS
%     若 mS<=k0*mG 且k1*sigmaG<=sigmaS<=k2*sigmaG,则 g(x,y)=E*f(x,y);否则 g(x,y)=f(x,y)
%     参数 E,k0,k1,k2取值如下:E=4.0,k0=0.4,k1=0.02,k2=0.4
% 示例:
%     I = imread('tungsten_original.tif');
%     J = x_histloceh(I,3);
%     figure,subplot(1,2,1),imshow(I);
%     subplot(1,2,2),imshow(J);
assert(isnumeric(A) && isreal(A) && ismatrix(A),'图像参数有误,必须是灰度图且像素值是实数');
assert(isnumeric(NEIBSIZE) && isreal(NEIBSIZE) && numel(NEIBSIZE)==1 &&...
    isequal(NEIBSIZE,round(NEIBSIZE)) && NEIBSIZE>=1 && mod(NEIBSIZE,2)==1 && NEIBSIZE<=min(size(A)),...
    '邻域大小参数有误,必须是不小于1且不大于图像行数和列数的奇数');
NEIBSIZE = double(NEIBSIZE);
[m,n] = size(A);    % 输入图像大小
neibr = (NEIBSIZE-1) / 2;    % 邻域半径
B = A;    % 初始化输出图像为输入图像
mg = mean(double(A(:)));    % 输入图像全局均值
sgmg = std(double(A(:)));    % 输入图像全局标准差

% 扩充输入图像
A = [zeros(neibr,n);A;zeros(neibr,n)];
A = [zeros(m + 2*neibr,neibr),A,zeros(m + 2*neibr,neibr)];

A = double(A);
for i = 1 + neibr:m + neibr
    for j = 1 + neibr:n + neibr
        temp=A(i - neibr:i + neibr,j - neibr:j + neibr);    % 邻域内所有像素值
        ms = mean(temp(:));    % 局部均值
        sgms = std(temp(:));    % 局部标准差
        if ms <=0.4 * mg && sgms>= 0.02 * sgmg && sgms<=0.4 * sgmg
            B(i - neibr,j - neibr)=4 * A(i,j);
        end
    end
end