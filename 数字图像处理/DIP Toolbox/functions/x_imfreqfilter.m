% @since 2017-12-26
function B = x_imfreqfilter(A,METHOD,D,R)
%% 频域滤波
% 函数说明:
%     B = x_imfreqfilter(im,METHOD,D,R)
%     A:图像(灰度图)
%     METHOD:滤波方式('idealL':理想低通滤波 'idealH':理想高通滤波
%         'butterworthL':布特沃斯低通滤波 'butterworthH':布特沃斯高通滤波
%         'gaussianL':高斯低通滤波 'gaussianH':高斯高通滤波)
%     D:截止频率    R:布特沃斯滤波阶数,正整数    B:频域滤波后的图像
% 原理或算法:
%     频域中心点(u,v)与频率矩形中心的距离 D(u,v),截止频率D
%     理想低通滤波:若D(u,v)<=D,H(u,v)=1;否则,H(u,v)=0
%     理想高通滤波:若D(u,v)<=D,H(u,v)=0;否则,H(u,v)=1
%     n阶布特沃斯低通滤波: H(u,v)=1/(1+(D(u,v)/D)^(2*n))
%     n阶布特沃斯高通滤波: H(u,v)=1/(1+(D/D(u,v))^(2*n))
%     高斯低通滤波: H(u,v)=exp(-(D(u,v)^2)/(2*D^2))
%     高斯高通滤波: H(u,v)=1-exp(-(D(u,v)^2)/(2*D^2))
% 示例:
%     I = imread('test_pattern_blurring_orig.tif');
%     D=60;    % 截止频率
%     R=2;    % 布特沃斯滤波器阶数
%     LI = x_imfreqfilter(I,'idealL',D);
%     HI =x_imfreqfilter(I,'idealH',D);
%     LB = x_imfreqfilter(I,'butterworthL',D,R);
%     HB = x_imfreqfilter(I,'butterworthH',D,R);
%     LG = x_imfreqfilter(I,'gaussianL',D);
%     HG = x_imfreqfilter(I,'gaussianH',D);
%     figure,subplot(3,3,2),imshow(I);
%     subplot(3,3,4),imshow(LI),title(['理想低通 D_0=' num2str(D)]);
%     subplot(3,3,5),imshow(LB),title(['布特沃斯低通 D_0=' num2str(D) ' 阶数' num2str(R)]);
%     subplot(3,3,6),imshow(LG),title(['高斯低通 D_0=' num2str(D)]);
%     subplot(3,3,7),imshow(HI),title(['理想高通 D_0=' num2str(D)]);
%     subplot(3,3,8),imshow(HB),title(['布特沃斯高通 D_0=' num2str(D) ' 阶数' num2str(R)]);
%     subplot(3,3,9),imshow(HG),title(['高斯高通 D_0=' num2str(D)]);
assert(isnumeric(A) && isreal(A) && ismatrix(A),...
    '图像参数有误,必须是灰度图且像素值是实数');
assert(strcmp(METHOD,'idealL') || strcmp(METHOD,'idealH') ||...
    strcmp(METHOD,'butterworthL') || strcmp(METHOD,'butterworthH') ||...
    strcmp(METHOD,'gaussianL') || strcmp(METHOD,'gaussianH'),...
    '滤波方式参数有误,必须是''idealL'',''idealH'',''butterworthL'',''butterworthH'',''gaussianL''或''gaussianH''');
assert(isnumeric(D) && isreal(D) && numel(D)==1,'截止频率参数有误,必须是实数');
D = double(D);
if nargin == 4
    assert(strcmp(METHOD,'butterworthL') || strcmp(METHOD,'butterworthH'),...
        '参数个数有误,移除最后一个参数');
    assert(isnumeric(R) && isreal(R) && numel(R)==1 && isequal(round(R),R) && R>0,...
        '布特沃斯滤波阶数参数有误,必须是正整数');
    R = double(R);
end
if nargin==3 && (strcmp(METHOD,'butterworthL') || strcmp(METHOD,'butterworthH'))    % 布特沃斯滤波默认阶数1
    R = 1;
end
[m,n] = size(A);    % 输入图像大小
p = 2*m;    % 填充后图像的行数
q = 2*n;    % 填充后图像的列数
M = zeros(p,q);    % 初始化填充后的图像
M(1:m,1:n) = A;    % 填充后的图像左上角是输入图像
for i = 1:m
    for j = 1:n
        M(i,j) = M(i,j) * (-1) ^ (i+j);
    end
end
F = fft2(M);    % 对填充后的图像进行傅里叶变换

% 计算填充后的图像中所有点到中心点的距离
[X,Y] = meshgrid(1:p,1:q);
u = X(:);
v = Y(:);
d = sqrt((u - p / 2).^2 + (v - q / 2).^2);
d = reshape(d,[q p])';

switch METHOD
    case 'idealL'    % 理想低通滤波
        h = d;
        h(h<=D) = 1;
        h(h>D) = 0;
    case 'idealH'    % 理想高通滤波
        h = d;
        h(h<=D) = 0;
        h(h>D) = 1;
    case 'butterworthL'    % 布特沃斯低通滤波
        h = 1./(1 + (d / D).^(2 * R));
    case 'butterworthH'    % 布特沃斯高通滤波
        h = 1./(1 + (D./d).^(2 * R));
    case 'gaussianL'    % 高斯低通滤波
        h = exp(-d.^2 / (2 * D^2));
    case 'gaussianH'    % 高斯高通滤波
        h = 1 - exp(-d.^2/(2 * D^2));
end
G = h.*F;    % 滤波
B = ifft2(G);    % 傅里叶逆变换
for i = 1:m
    for j = 1:n
        B(i,j) = B(i,j) * (-1) ^ (i + j);
    end
end
B = cast(abs(B(1:m,1:n)),'like',A);    % 提取左上角的图像