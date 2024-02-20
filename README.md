# RandomSeatGenerator

## 概述

Java版随机排座位程序

JRE Version：17+

JavaFX Version: 20.0.1

v1.0.0-1.1.2 在win64平台上不需要JavaFX，在其他平台上需要

v1.2.0-1.2.8 在所有平台上都需要JavaFX来运行

v1.3.0+ 在Windows和macOS上可以安装并直接运行应用程序，在linux_x64上需要jre但不需要JavaFX

v1.0.0-1.3.4 种子只能为long值

v1.3.5+ 种子可为任意字符串

## 命令行模式

v1.2.4+ 启动时加上“--nogui”参数可启动无命令行模式，无更多参数

v1.2.5+ 可用参数：

- --config-path <path> 指定配置文件位置（只生效一次）
- --seed value 默认为随机数/字符串
- --output-path <path> 指定导出目录（只生效一次）

v1.2.6+ 可用参数：

- --help 打印帮助信息后退出
- --license 打印开源声明（内容与“首选项”页面里的相同）后退出

## 配置文件格式：

### v1.5.0+

```json5
{
  // 行数
  "row_count": 7,
  // 列数，不可大于20
  "column_count": 7,
  // 随机轮换的行数
  "random_between_rows": 2,
  // 最后一排不可选的位置（从1到列数，其余值将会忽略）
  "last_row_pos_cannot_be_chosen": "1 2 7",
  // 按身高排序的人名列表
  "person_sort_by_height": "31 28 22 3 37 24 34 1 6 44 38 7 2 4 16 19 13 40 12 36 8 21 18 10 41 14 20 43 35 15 26 32 17 42 27 29 9 5 25 30 11 23 39 33",
  // 组长列表
  "group_leader_list": "2 4 10 16 19 20 24 25 26 27 28 29 30 31 32 33 34 39 44",
  // 拆分列表，每行一组
  "separate_list": "25 30",
  // 是否随机挑选一名幸运儿
  "lucky_option": false,
  // 是否导出到可写文件
  "export_writable": false
}
```

### v1.3.5+

```json5
{
  // 行数
  "row_count": "7",
  // 列数，不可大于20
  "column_count": "7",
  // 随机轮换的行数
  "random_between_rows": "2",
  // 最后一排不可选的位置（从1到列数，其余值将会忽略）
  "last_row_pos_cannot_be_chosen": "1 2 7",
  // 按身高排序的人名列表
  "person_sort_by_height": "31 28 22 3 37 24 34 1 6 44 38 7 2 4 16 19 13 40 12 36 8 21 18 10 41 14 20 43 35 15 26 32 17 42 27 29 9 5 25 30 11 23 39 33",
  // 组长列表
  "group_leader_list": "2 4 10 16 19 20 24 25 26 27 28 29 30 31 32 33 34 38 39 44",
  // 拆分列表，每行一组
  "separate_list": "25 30",
  // 是否随机挑选一名幸运儿
  "lucky_option": false,
  // 是否导出到可写文件
  "export_writable": false
}
```

### v1.2.7+

```json5
{
  // 行数
  "row_count": "7",
  // 列数，不可大于20
  "column_count": "7",
  // 随机轮换的行数
  "random_between_rows": "2",
  // 最后一排不可选的位置（从1到列数，其余值将会忽略）
  "last_row_pos_cannot_be_chosen": "1 2 7",
  // 按身高排序的人名列表
  "person_sort_by_height": "31 28 22 3 37 24 34 1 6 44 38 7 2 4 16 19 13 40 12 36 8 21 18 10 41 14 20 43 35 15 26 32 17 42 27 29 9 5 25 30 11 23 39 33",
  // 组长列表
  "group_leader_list": "2 4 10 16 19 20 23 24 25 26 27 28 29 30 31 32 33 34 38 39 43 44",
  // 拆分列表，每行一组
  "separate_list": "25 30",
  // 是否随机挑选一名幸运儿
  "lucky_option": false
}
```

### v1.2.2-1.2.6

```json5
{
  // 行数
  "row_count": "7",
  // 列数
  "column_count": "7",
  // 随机轮换的行数
  "random_between_rows": "2",
  // 最后一排可选的位置
  "last_row_pos_can_be_choosed": "3 4 5 6",
  // 按身高排序的人名列表
  "person_sort_by_height": "31 28 22 3 37 24 34 1 6 44 38 7 2 4 16 19 13 40 12 36 8 21 18 10 41 14 20 43 35 15 26 32 17 42 27 29 9 5 25 30 11 23 39 33",
  // 组长列表
  "group_leader_list": "2 4 10 16 19 20 23 24 25 26 27 28 29 30 31 32 33 34 38 39 43 44",
  // 拆分列表，每行一组
  "separate_list": "25 30",
  // 是否随机挑选一名幸运儿
  "lucky_option": false
}
```

### v1.2.1

```json5
{
  // 行数
  "rows": "7",
  // 列数
  "columns": "7",
  // 随机轮换的行数
  "random_between_rows": "2",
  // 最后一排可选的位置
  "last_row_pos_can_be_choosed": "3 4 5 6",
  // 按身高排序的人名列表
  "person_sort_by_height": "31 28 22 3 37 24 34 1 6 44 38 7 2 4 16 19 13 40 12 36 8 21 18 10 41 14 20 43 35 15 26 32 17 42 27 29 9 5 25 30 11 23 39 33",
  // 组长列表
  "zz": "2 4 10 16 19 20 23 24 25 26 27 28 29 30 31 32 33 34 38 39 43 44",
  // 拆分列表，每行一组
  "separate": "25 30",
  // 是否随机挑选一名幸运儿
  "lucky_option": false
}
```

### v1.2.0

```json5
{
  // 行数
  "rows": "7",
  // 列数
  "columns": "7",
  // 随机轮换的行数
  "random_between_rows": "2",
  // 最后一排可选的位置
  "last_row_pos_can_be_choosed": "3 4 5 6",
  // 按身高排序的人名列表
  "person_sort_by_height": "31 28 22 3 37 24 34 1 6 44 38 7 2 4 16 19 13 40 12 36 8 21 18 10 41 14 20 43 35 15 26 32 17 42 27 29 9 5 25 30 11 23 39 33",
  // 组长列表
  "zz": "2 4 10 16 19 20 23 24 25 26 27 28 29 30 31 32 33 34 38 39 43 44",
  // 拆分列表，每行一组
  "separate": "25 30"
}
```

### v1.0.0-1.1.2

```json5
{
  // 前两排名单
  "ot": "19 16 21 40 13 34 8 31 37 24 22 28 38 44",
  // 中两排名单
  "tf": "17 10 12 35 36 3 1 6 20 14 7 42 15 18",
  // 后两排名单
  "fs": "23 25 33 30 39 5 4 29 11 26 32 2 43 9 41 27",
  // 组长列表
  "zz": "2 4 10 16 19 20 23 24 25 26 27 28 29 30 31 32 33 34 38 39 43 44",
  // 拆分列表，每行一组
  "separate": "25 30"
}
```
