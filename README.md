# 简介

这是一个简易的编译器，暂时只实现了词法分析与语法分析。

词法分析用了龙书第二版后面的代码。

语法分析是学了龙书上构造SLR语法分析表后，自己写的。通过自底向上进行规约。

# 使用说明

1.txt为检验的代码

2.txt为文法

grammar类用来解析文法，文法以ε为空，→为推导符号，非终止符号为大写，终止符号为小写或字符

Parser类用来求解first、follow集，以及用来求SLR分析表

在Main方法中指定文法文件和代码文件，以及文法的开始符号

# 例子展示

>  文法

```
P → S
S → M | O
M → if (C) then M else M | B
O → if (C) then S | if (C) then M else O
B → id = E;
C → E > E
C → E < E
C → E  ==  E
E → E + T
E → E – T
E → T
T → F
T → T * F
T → T / F
F → ( E )
F → id
F → int10
```
    
> 分析代码段
```
if ( a == 4 ) then
    cc = 4;
else
    if ( b > c ) then
        ab = 6 + 4;
    else
        l = 5;
```

> first、follow集输出
```
FIRST(P)={id,if}
FIRST(B)={id}
FIRST(S)={id,if}
FIRST(C)={int10,(,id}
FIRST(T)={int10,(,id}
FIRST(E)={int10,(,id}
FIRST(F)={int10,(,id}
FIRST(M)={id,if}
FIRST(O)={if}

FOLLOW(P)={$}
FOLLOW(B)={$,else}
FOLLOW(S)={$}
FOLLOW(C)={)}
FOLLOW(T)={==,–,),*,;,+,<,>,/}
FOLLOW(E)={==,–,),;,+,<,>}
FOLLOW(F)={==,–,),*,;,+,<,>,/}
FOLLOW(M)={$,else}
FOLLOW(O)={$}
```

> closure集合输出
```
第0个状态
P → ·S 
S → ·M 
S → ·O 
M → ·if ( C ) then M else M 
M → ·B 
O → ·if ( C ) then S 
O → ·if ( C ) then M else O 
B → ·id = E ; 


第1个状态
M → if ·( C ) then M else M 
O → if ·( C ) then S 
O → if ·( C ) then M else O 


第2个状态
B → id ·= E ; 


第3个状态
P → S ·


第4个状态
S → M ·


第5个状态
S → O ·


第6个状态
M → B ·
……
省略
……
```
> 分析过程输出
```
(0 1 )     (if )
(0 1 7 )     (if ( )
(0 1 7 10 )     (if ( id )
(0 1 7 15 )     (if ( F )
(0 1 7 14 )     (if ( T )
(0 1 7 13 )     (if ( E )
(0 1 7 13 21 )     (if ( E == )
(0 1 7 13 21 11 )     (if ( E == int10 )
(0 1 7 13 21 15 )     (if ( E == F )
(0 1 7 13 21 14 )     (if ( E == T )
(0 1 7 13 21 31 )     (if ( E == E )
(0 1 7 12 )     (if ( C )
(0 1 7 12 18 )     (if ( C ) )
(0 1 7 12 18 28 )     (if ( C ) then )
(0 1 7 12 18 28 2 )     (if ( C ) then id )
(0 1 7 12 18 28 2 8 )     (if ( C ) then id = )
(0 1 7 12 18 28 2 8 11 )     (if ( C ) then id = int10 )
(0 1 7 12 18 28 2 8 15 )     (if ( C ) then id = F )
(0 1 7 12 18 28 2 8 14 )     (if ( C ) then id = T )
(0 1 7 12 18 28 2 8 16 )     (if ( C ) then id = E )
(0 1 7 12 18 28 2 8 16 26 )     (if ( C ) then id = E ; )
(0 1 7 12 18 28 6 )     (if ( C ) then B )
(0 1 7 12 18 28 36 )     (if ( C ) then M )
(0 1 7 12 18 28 36 38 )     (if ( C ) then M else )
(0 1 7 12 18 28 36 38 1 )     (if ( C ) then M else if )
(0 1 7 12 18 28 36 38 1 7 )     (if ( C ) then M else if ( )
(0 1 7 12 18 28 36 38 1 7 10 )     (if ( C ) then M else if ( id )
(0 1 7 12 18 28 36 38 1 7 15 )     (if ( C ) then M else if ( F )
(0 1 7 12 18 28 36 38 1 7 14 )     (if ( C ) then M else if ( T )
(0 1 7 12 18 28 36 38 1 7 13 )     (if ( C ) then M else if ( E )
(0 1 7 12 18 28 36 38 1 7 13 19 )     (if ( C ) then M else if ( E > )
(0 1 7 12 18 28 36 38 1 7 13 19 10 )     (if ( C ) then M else if ( E > id )
(0 1 7 12 18 28 36 38 1 7 13 19 15 )     (if ( C ) then M else if ( E > F )
(0 1 7 12 18 28 36 38 1 7 13 19 14 )     (if ( C ) then M else if ( E > T )
(0 1 7 12 18 28 36 38 1 7 13 19 29 )     (if ( C ) then M else if ( E > E )
(0 1 7 12 18 28 36 38 1 7 12 )     (if ( C ) then M else if ( C )
(0 1 7 12 18 28 36 38 1 7 12 18 )     (if ( C ) then M else if ( C ) )
(0 1 7 12 18 28 36 38 1 7 12 18 28 )     (if ( C ) then M else if ( C ) then )
(0 1 7 12 18 28 36 38 1 7 12 18 28 2 )     (if ( C ) then M else if ( C ) then id )
(0 1 7 12 18 28 36 38 1 7 12 18 28 2 8 )     (if ( C ) then M else if ( C ) then id = )
(0 1 7 12 18 28 36 38 1 7 12 18 28 2 8 11 )     (if ( C ) then M else if ( C ) then id = int10 )
(0 1 7 12 18 28 36 38 1 7 12 18 28 2 8 15 )     (if ( C ) then M else if ( C ) then id = F )
(0 1 7 12 18 28 36 38 1 7 12 18 28 2 8 14 )     (if ( C ) then M else if ( C ) then id = T )
(0 1 7 12 18 28 36 38 1 7 12 18 28 2 8 16 )     (if ( C ) then M else if ( C ) then id = E )
(0 1 7 12 18 28 36 38 1 7 12 18 28 2 8 16 22 )     (if ( C ) then M else if ( C ) then id = E + )
(0 1 7 12 18 28 36 38 1 7 12 18 28 2 8 16 22 11 )     (if ( C ) then M else if ( C ) then id = E + int10 )
(0 1 7 12 18 28 36 38 1 7 12 18 28 2 8 16 22 15 )     (if ( C ) then M else if ( C ) then id = E + F )
(0 1 7 12 18 28 36 38 1 7 12 18 28 2 8 16 22 32 )     (if ( C ) then M else if ( C ) then id = E + T )
(0 1 7 12 18 28 36 38 1 7 12 18 28 2 8 16 )     (if ( C ) then M else if ( C ) then id = E )
(0 1 7 12 18 28 36 38 1 7 12 18 28 2 8 16 26 )     (if ( C ) then M else if ( C ) then id = E ; )
(0 1 7 12 18 28 36 38 1 7 12 18 28 6 )     (if ( C ) then M else if ( C ) then B )
(0 1 7 12 18 28 36 38 1 7 12 18 28 36 )     (if ( C ) then M else if ( C ) then M )
(0 1 7 12 18 28 36 38 1 7 12 18 28 36 38 )     (if ( C ) then M else if ( C ) then M else )
(0 1 7 12 18 28 36 38 1 7 12 18 28 36 38 2 )     (if ( C ) then M else if ( C ) then M else id )
(0 1 7 12 18 28 36 38 1 7 12 18 28 36 38 2 8 )     (if ( C ) then M else if ( C ) then M else id = )
(0 1 7 12 18 28 36 38 1 7 12 18 28 36 38 2 8 11 )     (if ( C ) then M else if ( C ) then M else id = int10 )
(0 1 7 12 18 28 36 38 1 7 12 18 28 36 38 2 8 15 )     (if ( C ) then M else if ( C ) then M else id = F )
(0 1 7 12 18 28 36 38 1 7 12 18 28 36 38 2 8 14 )     (if ( C ) then M else if ( C ) then M else id = T )
(0 1 7 12 18 28 36 38 1 7 12 18 28 36 38 2 8 16 )     (if ( C ) then M else if ( C ) then M else id = E )
(0 1 7 12 18 28 36 38 1 7 12 18 28 36 38 2 8 16 26 )     (if ( C ) then M else if ( C ) then M else id = E ; )
(0 1 7 12 18 28 36 38 1 7 12 18 28 36 38 6 )     (if ( C ) then M else if ( C ) then M else B )
(0 1 7 12 18 28 36 38 1 7 12 18 28 36 38 39 )     (if ( C ) then M else if ( C ) then M else M )
(0 1 7 12 18 28 36 38 39 )     (if ( C ) then M else M )
(0 4 )     (M )
(0 3 )     (S )
匹配成功
```

