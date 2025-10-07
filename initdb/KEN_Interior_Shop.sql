-- データベースの作成 (存在しない場合)
CREATE DATABASE IF NOT EXISTS KEN_Interior_Shop;

-- 使用するデータベースを指定
USE KEN_Interior_Shop;

-- テーブルの削除 (もし既に存在する場合)
DROP TABLE IF EXISTS m_items, orders, order_details, shipping, category, members, m_staff;

-- members テーブルの作成
CREATE TABLE members (
  member_id INT(6) AUTO_INCREMENT,
  member_name CHAR(64) NOT NULL,
  member_kana CHAR(64) NOT NULL,
  mail VARCHAR(128) NOT NULL UNIQUE,
  password VARCHAR(128) NOT NULL,
  phone_number CHAR(64) NOT NULL UNIQUE,
  postal_code CHAR(7),
  address1 VARCHAR(255) NOT NULL,
  address2 VARCHAR(255) NOT NULL,
  address3 VARCHAR(255) NOT NULL,
  credit_no INT,
  cancel INT,
  PRIMARY KEY (member_id)
);

-- members テーブルにテストデータ追加
INSERT INTO members (member_name, member_kana, mail, password, phone_number, 
postal_code, address1, address2, address3, credit_no, cancel)
VALUES
 ( '山田太郎', 'ヤマダタロウ', 'yamada@mail.com', '1234', '0901234567', '1234567', 'A県',
 'B市C町', '123', 123456789, 0);


-- category テーブルの作成
CREATE TABLE category (
  category_id INT AUTO_INCREMENT,
  category_name CHAR(64) NOT NULL,
  PRIMARY KEY (category_id)
);

-- category テーブル初期データ登録
INSERT INTO category VALUES
(1, 'テーブル'),	
(2, '椅子'),	
(3, '本棚'),	
(4, 'ベッド'),	
(5, 'テーブルランプ'),	
(6, 'ソファ');	


-- m_items テーブルの作成
CREATE TABLE m_items (
  item_id INT AUTO_INCREMENT,
  item_name CHAR(255) NOT NULL,
  category_id INT,
  item_price INT NOT NULL,
  rs_date DATE,
  image CHAR(255),
  explanation TEXT,
  stock INT(10),
  PRIMARY KEY (item_id),
  FOREIGN KEY (category_id) REFERENCES category(category_id)
) ;

-- m_items 初期データ登録
INSERT INTO m_items VALUES 
(1, '最高品質のテーブル', 1, 30000, '2023/10/1', 'img/table001.png',
  '最高品質の木材を使った、当店最高品質のテーブルです。滑らかな手触りと重厚な感触をお楽しみください。',
10),
(2, '高品質のテーブル',	1, 15000, '2023/10/1', 'img/table002.png',
'高品質の木材を使ったテーブルです。弊社の社長も愛用しております。是非この機会にお買い求めください。',
10),
(3, '普通のテーブル', 1, 7500, '2023/10/1', 'img/table003.png',
'頑丈なので、成長期のお子様におススメです。',
10),
(4, '最高品質の椅子', 2, 30000, '2023/10/1', 'img/chair001.png',
'最高品質の椅子です。座っただけで天にも昇るような幸福感が得られます。',
10),
(5, '普通の椅子', 2, 7500, '2023/10/1', 'img/chair002.png',
'普通の椅子です。頑丈なので、成長期のお子様におススメです。',
10),
(6, '最高品質の本棚', 3, 30000, '2023/10/1', 'img/bookshelf001.png',
'フランス国際映画祭にノミネートされた映画にも登場した当店自慢の一点です。収納した本が木材の香りに包まれるため、良い読書ライフを送ることが可能です。'
,10),
(7, '普通の本棚', 3, 7500, '2023/10/1', 'img/bookshelf002.png',
'普通の本棚です。漫画本が500冊ほど収納できます。',
10),
(8, '超最高品質のベッド', 4, 60000, '2023/10/1', 'img/bed001.png',
'イギリス王室でも使われているベッドです。睡眠の質が大きく向上します。',
10),
(9, '最高品質のベッド', 4, 30000, '2023/10/1', 'img/bed002.png',
'ぐっすり眠って体力を回復できるベッドです。まるで自然の中で眠っているかのような安心感が売りです。',
10),
(10, '普通のベッド', 4, 7500, '2023/10/1', 'img/bed003.png',
'頑丈なので、成長期のお子様におススメです。',
10),
(11, '高品質のテーブルランプ', 5, 15000, '2023/10/1', 'img/lamp001.png',
'暖炉の炎をイメージした穏やかな光が売りの製品です。自宅が池のほとりにある木造の別荘と錯覚するほど',
10),
(12, '普通のテーブルランプ', 5, 7500, '2023/10/1', 'img/lamp002.png',
'明るさを10段階で調整できます。様々な場所で活用できるテーブルランプです。', 
10),
(13, '最高品質のソファ', 6, 30000, '2024/1/4', 'img/sofa001.png',
'ノイシュヴァンシュタイン城で使われていたソファを再現したものです。', 
10),
(14, '高品質のソファ', 6, 15000, '2024/1/4', 'img/sofa002.png',
'2人掛けのソファです。リビングに配置してテレビを見るのがおススメです。',
10) ;

-- shipping テーブルの作成
CREATE TABLE shipping (
  shipping_id INT AUTO_INCREMENT,
  shipping_name CHAR(64) NOT NULL,
  shipping_kana CHAR(64) NOT NULL,
  shipping_phone CHAR(64) NOT NULL,
  shipping_postal_code CHAR(7),
  shipping_address1 VARCHAR(255) NOT NULL,
  shipping_address2 VARCHAR(255) NOT NULL,
  shipping_address3 VARCHAR(255) NOT NULL,
  PRIMARY KEY (shipping_id)
);

-- orders テーブルの作成
CREATE TABLE orders (
  order_id INT AUTO_INCREMENT,
  member_id INT NOT NULL,
  total INT NOT NULL,
  order_date DATE NOT NULL,
  payment ENUM ('現金', 'クレジット'),
  shipping_id INT,
  shipping_frag ENUM ('未発送', '発送済'),
  PRIMARY KEY (order_id),
  FOREIGN KEY (member_id) REFERENCES members(member_id),
  FOREIGN KEY (shipping_id) REFERENCES shipping(shipping_id)
);

-- order_details テーブルの作成
CREATE TABLE order_details (
  detail_id INT AUTO_INCREMENT,
  order_id INT NOT NULL,
  item_id INT NOT NULL,
  item_quantity INT NOT NULL,
  subtotal INT,
  PRIMARY KEY (detail_id),
  FOREIGN KEY (order_id) REFERENCES orders (order_id),
  FOREIGN KEY (item_id) REFERENCES m_items (item_id)
);

-- m_staff テーブルの作成
CREATE TABLE m_staff (
  staff_id INT(4) NOT NULL,
  staff_name CHAR(64) NOT NULL,
  password CHAR(64) NOT NULL,
  administrator ENUM('従業員', '管理者'),
  PRIMARY KEY (staff_id)
);


-- m_staff 初期データ登録
INSERT INTO m_staff VALUES (9999, 'test', 'test', 1);
