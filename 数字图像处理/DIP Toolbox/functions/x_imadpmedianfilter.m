% @since 2017-12-26
function B = x_imadpmedianfilter(A,WINSIZE,MAXINCR)
%% 自适应中值滤波
% 函数说明:
%     B = x_imadpmedianfilter(A,WINSIZE,MAXINCR)
%     A:图像(灰度图或彩色图)    WINSIZE:每次处理的窗口大小    Q:最大窗口大小增量
%     B:自适应中值滤波后的图像
% 原理或算法:
%     见源码
% 示例:
%     I = imread('ckt-board-saltpep-prob.pt05.tif');
%     J = x_imadpmedianfilter(I,3,4);
%     figure,subplot(1,2,1),imshow(I);
%     subplot(1,2,2),imshow(J);
assert(isnumeric(A) && isreal(A) && (ismatrix(A) || ndims(A)==3),...
    '图像参数有误,必须是灰度图或彩色图且像素值是实数');
assert(isnumeric(WINSIZE) && isreal(WINSIZE) &&...
    (numel(WINSIZE)==1 || numel(WINSIZE)==2) &&...
        sum(WINSIZE>0)==length(WINSIZE) && isequal(WINSIZE,round(WINSIZE)),...
    '窗口大小参数有误,必须是整数或长度为2的整数(行或列)向量');
assert(isnumeric(MAXINCR) && isreal(MAXINCR) && numel(MAXINCR) == 1 &&...
    isequal(round(MAXINCR),MAXINCR) && MAXINCR>=0,...
    '窗口大小最大增量参数有误,必须是非负整数');
if numel(WINSIZE) == 1
    WINSIZE = [WINSIZE WINSIZE];
end
WINSIZE = double(WINSIZE);
MAXINCR = double(MAXINCR);
[m,n,r] = size(A);    % 输入图像大小
B = cast(zeros(m,n,r),'like',A);
p = WINSIZE(1);
q = WINSIZE(2);

% 扩充图像
if p > 1
    v = round((p - 1) / 2);
    u = p - 1 - v;
    A = [zeros(u,size(A,2),r);A;zeros(v,size(A,2),r)];
end
if q > 1
    v = round((q - 1) / 2);
    u = q - 1 - v;
    A=[zeros(size(A,1),u,r),A,zeros(size(A,1),v,r)];
end

A = double(A);
for i = 1:m
    for j = 1:n
        for k = 1:r
            W = WINSIZE;
            while true
                temp = A(i:i + W(1) - 1,j:j + W(2) - 1,k);
                temp = temp(:);
                Zmin = min(temp);
                Zmax = max(temp);
                Zmed = median(temp);
                if Zmed - Zmin>0 && Zmed - Zmax<0
                    if A(i,j,k) - Zmin>0 && A(i,j,k) - Zmax<0
                        B(i,j,k) = A(i,j,k);
                    else
                        B(i,j,k) = Zmed;
                    end
                    break;
                else
                    W = W +1;
                    if W(1) - WINSIZE(1)>MAXINCR || i + W(1)>m + p || j + W(2)>n + q
                        B(i,j,k) = Zmed;
                        break;
                    end
                end
            end
        end
    end
end