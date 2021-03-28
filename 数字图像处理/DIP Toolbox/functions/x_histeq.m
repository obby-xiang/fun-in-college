% @since 2017-12-26
function B = x_histeq(A,WINSIZE,METHOD)
%% 直方图均衡
% 函数说明:
%     B = x_histeq(A,WINSIZE,METHOD)
%     A:图像(灰度图、彩色图)    WINSIZE:(灰度图)局部直方图均衡,每次处理的窗口大小
%     METHOD:(灰度图)局部直方图均衡处理窗口移动方式('step':窗口逐行逐列移动 'skip':窗口跨窗口行窗口列移动),默认是'skip'
%     B:直方图均衡化后的图像
% 原理或算法:
%     灰度图直方图均衡:
%         MN为图像的总像素,nk是灰度级为rk的像素个数
%         则图像中灰度级rk出现的概率近似为pr(rk)=nk/MN (k=0,1,2,...,255)
%         直方图均衡的变换函数为T(rk)=255*(pr(r0)+pr(r1)+pr(r2)+...+pr(rk)) (k=0,1,2,...,255)
%     灰度图局部直方图均衡:窗口不断移动的同时对窗口内的像素进行上述直方图均衡处理
%     彩色图直方图均衡:在图像HSI模型的I分量上进行和灰度图直方图均衡一样的处理
% 示例:
%     I = imread('washed_out_pollen_image.tif');
%     M = imread('embedded_square_noisy_512.tif');
%     J = x_histeq(I);
%     N = x_histeq(M);
%     P = x_histeq(M,3,'step');    % 局部直方图均衡,处理窗口逐行逐列移动
%     Q = x_histeq(M,[3 3]);    % 局部直方图均衡,处理窗口跨窗口行跨窗口列移动
%     figure,subplot(3,2,1),imshow(I),subplot(3,2,2),imshow(J);
%     subplot(3,2,3),imshow(M),subplot(3,2,4),imshow(N);
%     subplot(3,2,5),imshow(P),title('处理窗口逐行逐列移动');
%     subplot(3,2,6),imshow(Q),title('处理窗口跨窗口行跨窗口列移动');
%     R = imread('caster_stand_original.tif');
%     S = x_histeq(R);
%     figure,subplot(2,2,1),imshow(R);
%     subplot(2,2,3),imshow(S);
%     G = rgb2hsv(R);
%     subplot(2,2,2),imhist(G(:,:,3)),title('图像HSI模型的I分量直方图');
%     H = rgb2hsv(S);
%     subplot(2,2,4),imhist(H(:,:,3)),title('均衡化后图像HSI模型的I分量直方图');
assert(isa(A,'uint8') && isreal(A) && (ismatrix(A) || ndims(A)==3),'图像参数有误,必须是灰度图或彩色图且像素值数据类型是uint8');
if nargin >= 2
    assert(ismatrix(A),'不允许对彩色图进行局部直方图均衡');
    assert(isnumeric(WINSIZE) && isreal(WINSIZE) &&...
        (numel(WINSIZE)==1 || numel(WINSIZE)==2) &&...
        sum(WINSIZE>0)==length(WINSIZE) && isequal(WINSIZE,round(WINSIZE)),...
        '窗口大小参数有误,必须是整数或长度为2的整数(行或列)向量');
    if numel(WINSIZE) == 1
        WINSIZE = [WINSIZE WINSIZE];
    end
    WINSIZE = double(WINSIZE);
    if nargin == 2    % 默认处理窗口移动方式
        METHOD = 'skip';
    else
        assert(strcmp(METHOD,'step')||strcmp(METHOD,'skip'),'处理窗口移动方式参数有误,必须是''step''或''skip''')
    end
end
[m,n,r] = size(A);    % 输入图像大小
if nargin>=2    % 局部直方图均衡
    p = WINSIZE(1);    % 窗口行数
    q = WINSIZE(2);    % 窗口列数
    B = uint8(zeros(m,n));    % 初始化直方图均衡后的图像
    if strcmp(METHOD,'step')
        dx = 1;
        dy = 1;
    else
        dx = p;
        dy = q;
    end
    for i = 1:dx:m - p + 1
        for j = 1:dy:n - q + 1
            B(i:i + p - 1,j:j + q - 1) = x_histeq(A(i:i + p - 1,j:j + q - 1));    % 局部直方图均衡
        end
    end
    
    % 处理图像不足处理窗口跨行跨列部分
    xend = m - dx + 1;
    yend = n - dy + 1;
    if mod(m - p,dx) ~= 0 && xend >= 1
        for j = 1:dy:n - q + 1
            B(xend:xend + p - 1,j:j + q - 1) = x_histeq(A(xend:xend + p - 1,j:j + q - 1));    % 局部直方图均衡
        end
    end
    if mod(n - q,dy) ~= 0 && yend >= 1
        for i = 1:dx:m - p + 1
            B(i:i + p - 1,yend:yend + q - 1) = x_histeq(A(i:i + p - 1,yend:yend + q - 1));    % 局部直方图均衡
        end
    end
    if mod(m - p,dx) ~= 0 && xend >= 1 && mod(n - q,dy) ~= 0 && yend >= 1
        B(xend:xend + p - 1,yend:yend + q - 1) = x_histeq(A(xend:xend + p - 1,yend:yend + q - 1));    % 局部直方图均衡
    end
    
    return;
end
if r == 1    % 灰度图
    vct = double(A(:)) + 1;
else    % 彩色图,图像转换为HSI图像后再处理I分量
    H = rgb2hsv(A);
    v = round(H(:,:,3) * 255);    % 对I分量所有值映射到整数0至255
    vct = v(:) + 1;
end
povcount = zeros(1,256);    % 灰度值(灰度图)或亮度(彩色图)

% 统计对应灰度值(灰度图)或亮度(彩色图)的像素个数
for i = 1:m * n
    povcount(vct(i)) = povcount(vct(i)) + 1;
end

povprob = povcount / (m * n);    % 对应灰度值(灰度图)或亮度(彩色图)出现的概率
sr = zeros(1,256);    % 初始化变换函数

% 计算变换函数的值
sr(1) = 255 * povprob(1);
for i = 2:256
    sr(i) = sr(i - 1) + 255 * povprob(i);
end

if r == 1    % 灰度图
    B = uint8(zeros(m,n,r));    % 初始化直方图均衡后的图像
    sr = round(sr);    % 变换函数值取整
    for i = 1:m
        for j = 1:n
            B(i,j) = sr(A(i,j) + 1);
        end
    end
else    % 彩色图
    sr = sr / 255;    % 均一化变换函数值
    for i = 1:m
        for j = 1:n
            v(i,j) = sr(v(i,j) + 1);
        end
    end
    H(:,:,3) = v;
    B = im2uint8(hsv2rgb(H));
end