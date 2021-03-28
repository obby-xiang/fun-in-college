% @since 2017-12-26
function B = im_affinetransf_bilinear(A,T,ROW,COL)
%% 双线性插值
% 函数说明:
%     B = im_affinetransf_bilinear(A,T,ROW,COL)
%     A:图像(灰度图或彩色图)    T:仿射矩阵    ROW:输出图像的行数    COL:输出图像的列数
%     B:仿射变换后的图像
% 原理或算法:
%     记变换后图像g(x,y)中的坐标(i,j)对应原图像f(x,y)中的坐标(p+u,q+v),其中p,q为整数部分,u,v为小数部分
%     则g(i,j) = f(p,q)*(1-u)*(1-v) + f(p+1,q)*u*(1-v) + f(p,q+1)*(1-u)*v + f(p+1,q+1)*u*v
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
X = reshape(X,[COL ROW])';
Y = reshape(Y,[COL ROW])';

% 输入图像右、下各增加一列、行(赋值为输入图像的最后一列、行的像素值)
A = [A;A(m,:,:)];
A = [A,A(:,n,:)];
A(m+1,n+1,:) = A(m,n,:);

A = double(A);    % double化输入图像像素值
for i = 1:ROW
    for j = 1:COL
        if X(i,j)~=0 && Y(i,j)~=0   % 坐标在输入图像范围内
            xint = floor(X(i,j));
            yint = floor(Y(i,j));
            xdou = X(i,j) - xint;
            ydou = Y(i,j) - yint;
            for k = 1:r
                % 双线性插值
                B(i,j,k) = (1 - xdou) * (1 - ydou) * A(xint,yint,k) + ...
                    xdou * (1 - ydou) * A(xint + 1,yint,k) + ...
                    (1 - xdou) * ydou * A(xint,yint + 1,k) + ...
                    xdou * ydou * A(xint + 1,yint + 1,k);
            end
        end
    end
end