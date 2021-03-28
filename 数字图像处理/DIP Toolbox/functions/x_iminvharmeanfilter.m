% @since 2017-12-26
function B = x_iminvharmeanfilter(A,WINSIZE,Q)
%% 逆谐波均值滤波
% 函数说明:
%     B = x_iminvharmeanfilter(A,WINSIZE,Q)
%     A:图像(灰度图或彩色图)    WINSIZE:每次处理的窗口大小    Q:滤波器的阶数
%     B:逆谐波均值滤波后的图像
% 原理或算法:
%     f(x,y)=sum(g(x:x+s,y:y+t)^(q+1))/sum(g(x:x+s,y:y+t)^q)
% 示例:
%     I = imread('circuit-board-pepper-prob-pt1.tif');
%     M = imread('circuit-board-salt-prob-pt1.tif');
%     J = x_iminvharmeanfilter(I,[3 3],1.5);
%     N = x_iminvharmeanfilter(M,[3 3],-1.5);
%     figure,subplot(2,2,1),imshow(I),title('概率0.1的胡椒噪声污染');
%     subplot(2,2,2),imshow(J),title('3×3滤波器 Q=1.5');
%     subplot(2,2,3),imshow(M),title('概率0.1的盐噪声污染');
%     subplot(2,2,4),imshow(N),title('3×3滤波器 Q=-1.5');
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
assert(isnumeric(Q) && isreal(Q) && numel(Q)==1,'滤波器阶数参数有误,必须是实数');
Q = double(Q);
B = cast(x_imfilter(double(A).^(Q+1),ones(WINSIZE))./x_imfilter(double(A).^Q,ones(WINSIZE)),'like',A);