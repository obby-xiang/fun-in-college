% @since 2017-12-26
function B = im_affinetransf_nearest(A,T,ROW,COL)
%% 最近邻插值
% 函数说明:
%     B = im_affinetransf_nearest(A,T,ROW,COL)
%     A:图像(灰度图或彩色图)    T:仿射矩阵    ROW:输出图像的行数    COL:输出图像的列数
%     B:仿射变换后的图像
% 原理或算法:
%     记变换后图像g(x,y)中的坐标(i,j)对应原图像f(x,y)中的坐标(x,y),其中x,y可以是小数
%     将原图像中离(x,y)最近的整数点的像素值赋给g(i,j)
[m,n,r] = size(A);    % 输入图像大小
B = cast(zeros(ROW,COL,r),'like',A);    % 初始化输出图像
[X,Y] = meshgrid(1:ROW,1:COL);
intpos = [X(:) Y(:)];    % 输出图像中的所有整数点
intpos = [intpos ones(ROW * COL,1)];
invpos = intpos / T;    % 逆变换
X = invpos(:,1);
Y = invpos(:,2);

% 处理逆变换后得到的坐标
X(X>=0 & X<1) = 1;
X(X>m & X<=m+1) = m;
X(X<0 | X>m+1) = 0;
Y(Y>=0 & Y<1) = 1;
Y(Y>n & Y<=n+1) = n;
Y(Y<0 | Y>n+1) = 0;
X = round(reshape(X,[COL ROW])');
Y = round(reshape(Y,[COL ROW])');

for i = 1:ROW
    for j = 1:COL
        if X(i,j)~=0 && Y(i,j)~=0   % 坐标在输入图像范围内
            for k = 1:r
                % 最近邻插值
                B(i,j,k) = A(X(i,j),Y(i,j),k);
            end
        end
    end
end