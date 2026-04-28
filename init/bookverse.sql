CREATE DATABASE  IF NOT EXISTS `bookverse` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `bookverse`;
-- MySQL dump 10.13  Distrib 8.0.36, for Win64 (x86_64)
--
-- Host: localhost    Database: bookverse
-- ------------------------------------------------------
-- Server version	8.0.46

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `author_book`
--

DROP TABLE IF EXISTS `author_book`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `author_book` (
  `book_id` bigint NOT NULL,
  `author_id` bigint NOT NULL,
  KEY `idx_author_book_book_id` (`book_id`),
  KEY `idx_author_book_author_id` (`author_id`),
  CONSTRAINT `FK7cqs8nb7l859jcwwqoawcokqf` FOREIGN KEY (`author_id`) REFERENCES `authors` (`id`),
  CONSTRAINT `FKmeehr164a2cpxegeiawuv40a3` FOREIGN KEY (`book_id`) REFERENCES `books` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `author_book`
--

LOCK TABLES `author_book` WRITE;
/*!40000 ALTER TABLE `author_book` DISABLE KEYS */;
INSERT INTO `author_book` VALUES (1,1),(2,1),(3,2),(4,3),(5,4),(6,5),(7,6),(8,7),(9,8),(10,9),(11,10),(12,1),(13,11),(14,12),(15,1),(16,13),(17,14),(18,15),(19,16),(20,17);
/*!40000 ALTER TABLE `author_book` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `authors`
--

DROP TABLE IF EXISTS `authors`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `authors` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `avatar` varchar(255) DEFAULT NULL,
  `birthday` datetime(6) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `nationality` varchar(255) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `updated_by` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_authors_name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `authors`
--

LOCK TABLES `authors` WRITE;
/*!40000 ALTER TABLE `authors` DISABLE KEYS */;
INSERT INTO `authors` VALUES (1,'1776746859395-nguyen-nhat-anh.jpg','1955-05-06 08:00:00.000000','2026-04-03 20:53:42.000000','system','Nguyễn Nhật Ánh','Việt Nam','2026-04-21 04:47:39.427844','admin@bookverse.com'),(2,NULL,'1912-10-20 00:00:00.000000','2026-04-03 20:53:42.000000','system','Vũ Trọng Phụng','Việt Nam',NULL,NULL),(3,NULL,'1893-06-10 00:00:00.000000','2026-04-03 20:53:42.000000','system','Ngô Tất Tố','Việt Nam',NULL,NULL),(4,NULL,'1947-08-24 00:00:00.000000','2026-04-03 20:53:42.000000','system','Paulo Coelho','Brazil',NULL,NULL),(5,NULL,'1888-11-24 00:00:00.000000','2026-04-03 20:53:42.000000','system','Dale Carnegie','Mỹ',NULL,NULL),(6,NULL,'1883-10-26 00:00:00.000000','2026-04-03 20:53:42.000000','system','Napoleon Hill','Mỹ',NULL,NULL),(7,NULL,'1947-04-08 00:00:00.000000','2026-04-03 20:53:42.000000','system','Robert Kiyosaki','Mỹ',NULL,NULL),(8,NULL,'1977-07-20 00:00:00.000000','2026-04-03 20:53:42.000000','system','Timothy Ferriss','Mỹ',NULL,NULL),(9,NULL,'1874-11-07 00:00:00.000000','2026-04-03 20:53:42.000000','system','George S. Clason','Mỹ',NULL,NULL),(10,NULL,'1920-09-27 00:00:00.000000','2026-04-03 20:53:42.000000','system','Tô Hoài','Việt Nam',NULL,NULL),(11,NULL,'1900-06-29 00:00:00.000000','2026-04-03 20:53:42.000000','system','Antoine de Saint-Exupéry','Pháp',NULL,NULL),(12,NULL,'1933-08-09 00:00:00.000000','2026-04-03 20:53:42.000000','system','Tetsuko Kuroyanagi','Nhật Bản',NULL,NULL),(13,NULL,'1932-10-24 00:00:00.000000','2026-04-03 20:53:42.000000','system','Stephen R. Covey','Mỹ',NULL,NULL),(14,NULL,'1966-03-01 00:00:00.000000','2026-04-03 20:53:42.000000','system','David J. Lieberman','Mỹ',NULL,NULL),(15,NULL,'1974-01-01 00:00:00.000000','2026-04-03 20:53:42.000000','system','Charles Duhigg','Mỹ',NULL,NULL),(16,NULL,'1934-03-05 00:00:00.000000','2026-04-03 20:53:42.000000','system','Daniel Kahneman','Israel',NULL,NULL),(17,NULL,'1986-01-22 00:00:00.000000','2026-04-03 20:53:42.000000','system','James Clear','Mỹ',NULL,NULL);
/*!40000 ALTER TABLE `authors` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `book_images`
--

DROP TABLE IF EXISTS `book_images`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `book_images` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `is_primary` bit(1) NOT NULL,
  `relative_path` varchar(512) NOT NULL,
  `sort_order` int NOT NULL,
  `book_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_book_images_book_sort` (`book_id`,`sort_order`),
  CONSTRAINT `FKcnpy06tjmrsjisjf2bqpuvvbl` FOREIGN KEY (`book_id`) REFERENCES `books` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=106 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `book_images`
--

LOCK TABLES `book_images` WRITE;
/*!40000 ALTER TABLE `book_images` DISABLE KEYS */;
INSERT INTO `book_images` VALUES (1,_binary '','1775225121726-0-cho_toi_xin_mot_1.png',0,1),(2,_binary '\0','1775225121726-1-cho_toi_xin_mot_2.png',1,1),(3,_binary '\0','1775225121726-2-cho_toi_xin_mot_3.png',2,1),(4,_binary '\0','1775225121726-3-cho_toi_xin_mot_4.png',3,1),(5,_binary '\0','1775225121726-4-cho_toi_xin_mot_5.png',4,1),(11,_binary '\0','1775225313589-0-toi_thay_hoa_vang_tren_co_1.png',0,2),(12,_binary '','1775225313589-1-toi_thay_hoa_vang_tren_co_2.png',1,2),(13,_binary '\0','1775225313589-2-toi_thay_hoa_vang_tren_co_3.png',2,2),(14,_binary '\0','1775225313589-3-toi_thay_hoa_vang_tren_co_4.png',3,2),(15,_binary '\0','1775225313589-4-toi_thay_hoa_vang_tren_co_5.png',4,2),(16,_binary '','1775225489735-0-so_do_1.png',0,3),(17,_binary '\0','1775225489735-1-so_do_2.png',1,3),(18,_binary '\0','1775225489735-2-so_do_3.png',2,3),(19,_binary '\0','1775225489735-3-so_do_4.png',3,3),(20,_binary '\0','1775225489735-4-so_do_5.png',4,3),(21,_binary '','1775225602664-0-tat_den_1.png',0,4),(22,_binary '\0','1775225602664-1-tat_den_2.png',1,4),(23,_binary '\0','1775225602664-2-tat_den_3.png',2,4),(24,_binary '\0','1775225602664-3-tat_den_4.png',3,4),(25,_binary '\0','1775225602664-4-tat_den_5.png',4,4),(26,_binary '','1775225707938-0-nha_gia_kim_1.png',0,5),(27,_binary '\0','1775225707938-1-nha_gia_kim_2.png',1,5),(28,_binary '\0','1775225707938-2-nha_gia_kim_3.png',2,5),(29,_binary '\0','1775225707938-3-nha_gia_kim_4.png',3,5),(30,_binary '\0','1775225707938-4-nha_gia_kim_5.png',4,5),(31,_binary '','1775226192818-0-dac_nhan_tam_1.png',0,6),(32,_binary '\0','1775226192818-1-dac_nhan_tam_2.png',1,6),(33,_binary '\0','1775226192818-2-dac_nhan_tam_3.png',2,6),(34,_binary '\0','1775226192818-3-dac_nhan_tam_4.png',3,6),(35,_binary '\0','1775226192818-4-dac_nhan_tam_5.png',4,6),(36,_binary '','1775226295209-0-nghi_giau_lam_giau_1.png',0,7),(37,_binary '\0','1775226295209-1-nghi_giau_lam_giau_2.png',1,7),(38,_binary '\0','1775226295209-2-nghi_giau_lam_giau_3.png',2,7),(39,_binary '\0','1775226295209-3-nghi_giau_lam_giau_4.png',3,7),(40,_binary '\0','1775226295209-4-nghi_giau_lam_giau_5.png',4,7),(41,_binary '','1775226455927-0-cha_giau_cha_ngheo_1.png',0,8),(42,_binary '\0','1775226455927-1-cha_giau_cha_ngheo_2.png',1,8),(43,_binary '\0','1775226455927-2-cha_giau_cha_ngheo_3.png',2,8),(44,_binary '\0','1775226455927-3-cha_giau_cha_ngheo_4.png',3,8),(45,_binary '\0','1775226455927-4-cha_giau_cha_ngheo_5.png',4,8),(46,_binary '','1775226564695-0-tuan_lam_viec_4_gio_1.png',0,9),(47,_binary '\0','1775226564695-1-tuan_lam_viec_4_gio_2.png',1,9),(48,_binary '\0','1775226564695-2-tuan_lam_viec_4_gio_3.png',2,9),(49,_binary '\0','1775226564695-3-tuan_lam_viec_4_gio_4.png',3,9),(50,_binary '\0','1775226564695-4-tuan_lam_viec_4_gio_5.png',4,9),(51,_binary '','1775227358679-0-nguoi_giau_co_nhat_thanh_1.png',0,10),(52,_binary '\0','1775227358679-1-nguoi_giau_co_nhat_thanh_2.png',1,10),(53,_binary '\0','1775227358679-2-nguoi_giau_co_nhat_thanh_3.png',2,10),(54,_binary '\0','1775227358679-3-nguoi_giau_co_nhat_thanh_4.png',3,10),(55,_binary '\0','1775227358679-4-nguoi_giau_co_nhat_thanh_5.png',4,10),(56,_binary '','1776500316788-0-de_men_phieu_luu_ky_1.png',0,11),(57,_binary '\0','1776500316788-1-de_men_phieu_luu_ky_2.png',1,11),(58,_binary '\0','1776500316788-2-de_men_phieu_luu_ky_3.png',2,11),(59,_binary '\0','1776500316788-3-de_men_phieu_luu_ky_4.png',3,11),(60,_binary '\0','1776500316788-4-de_men_phieu_luu_ky_5.png',4,11),(61,_binary '','1776500562509-0-kinh_van_hoa_1.png',0,12),(62,_binary '\0','1776500562509-1-kinh_van_hoa_2.png',1,12),(63,_binary '\0','1776500562509-2-kinh_van_hoa_3.png',2,12),(64,_binary '\0','1776500562509-3-kinh_van_hoa_4.png',3,12),(65,_binary '\0','1776500562509-4-kinh_van_hoa_5.png',4,12),(66,_binary '','1776500668066-0-hoang_tu_be_1.png',0,13),(67,_binary '\0','1776500668066-1-hoang_tu_be_2.png',1,13),(68,_binary '\0','1776500668066-2-hoang_tu_be_3.png',2,13),(69,_binary '\0','1776500668066-3-hoang_tu_be_4.png',3,13),(70,_binary '\0','1776500668066-4-hoang_tu_be_5.png',4,13),(71,_binary '','1776500765012-0-totto_chan_ben_cua_so_1.png',0,14),(72,_binary '\0','1776500765012-1-totto_chan_ben_cua_so_2.png',1,14),(73,_binary '\0','1776500765012-2-totto_chan_ben_cua_so_3.png',2,14),(74,_binary '\0','1776500765012-3-totto_chan_ben_cua_so_4.png',3,14),(75,_binary '\0','1776500765012-4-totto_chan_ben_cua_so_5.png',4,14),(76,_binary '','1776500914359-0-chu_be_rac_roi_1.png',0,15),(77,_binary '\0','1776500914359-1-chu_be_rac_roi_2.png',1,15),(78,_binary '\0','1776500914359-2-chu_be_rac_roi_3.png',2,15),(79,_binary '\0','1776500914359-3-chu_be_rac_roi_4.png',3,15),(80,_binary '\0','1776500914359-4-chu_be_rac_roi_5.png',4,15),(81,_binary '','1776501014076-0-7_thoi_quen_hieu_qua_1.png',0,16),(82,_binary '\0','1776501014076-1-7_thoi_quen_hieu_qua_2.png',1,16),(83,_binary '\0','1776501014076-2-7_thoi_quen_hieu_qua_3.png',2,16),(84,_binary '\0','1776501014076-3-7_thoi_quen_hieu_qua_4.png',3,16),(85,_binary '\0','1776501014076-4-7_thoi_quen_hieu_qua_5.png',4,16),(86,_binary '','1776501589882-0-doc_vi_bat_ki_ai_1.png',0,17),(87,_binary '\0','1776501589882-1-doc_vi_bat_ki_ai_2.png',1,17),(88,_binary '\0','1776501589882-2-doc_vi_bat_ki_ai_3.png',2,17),(89,_binary '\0','1776501589882-3-doc_vi_bat_ki_ai_4.png',3,17),(90,_binary '\0','1776501589882-4-doc_vi_bat_ki_ai_5.png',4,17),(91,_binary '','1776501730014-0-suc_manh_cua_thoi_quen_1.png',0,18),(92,_binary '\0','1776501730014-1-suc_manh_cua_thoi_quen_2.png',1,18),(93,_binary '\0','1776501730014-2-suc_manh_cua_thoi_quen_3.png',2,18),(94,_binary '\0','1776501730014-3-suc_manh_cua_thoi_quen_4.png',3,18),(95,_binary '\0','1776501730014-4-suc_manh_cua_thoi_quen_5.png',4,18),(96,_binary '','1776501814881-0-tu_duy_nhanh_va_cham_1.png',0,19),(97,_binary '\0','1776501814881-1-tu_duy_nhanh_va_cham_2.png',1,19),(98,_binary '\0','1776501814881-2-tu_duy_nhanh_va_cham_3.png',2,19),(99,_binary '\0','1776501814881-3-tu_duy_nhanh_va_cham_4.png',3,19),(100,_binary '\0','1776501814881-4-tu_duy_nhanh_va_cham_5.png',4,19),(101,_binary '','1776501932952-0-thay_doi_ti_hon_1.png',0,20),(102,_binary '\0','1776501932952-1-thay_doi_ti_hon_2.png',1,20),(103,_binary '\0','1776501932952-2-thay_doi_ti_hon_3.png',2,20),(104,_binary '\0','1776501932952-3-thay_doi_ti_hon_4.png',3,20),(105,_binary '\0','1776501932952-4-thay_doi_ti_hon_5.png',4,20);
/*!40000 ALTER TABLE `book_images` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `books`
--

DROP TABLE IF EXISTS `books`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `books` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `cover_format` enum('HARDCOVER','PAPERBACK') DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `description` mediumtext NOT NULL,
  `dimensions` varchar(255) DEFAULT NULL,
  `discount` int NOT NULL,
  `image` varchar(255) DEFAULT NULL,
  `number_of_pages` int NOT NULL,
  `price` double NOT NULL,
  `publish_year` int NOT NULL,
  `quantity` bigint NOT NULL,
  `sold` bigint NOT NULL,
  `title` varchar(255) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `updated_by` varchar(255) DEFAULT NULL,
  `weight` double NOT NULL,
  `category_id` bigint DEFAULT NULL,
  `publisher_id` bigint DEFAULT NULL,
  `supplier_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_books_title` (`title`),
  KEY `idx_books_created_at` (`created_at`),
  KEY `idx_books_category_id` (`category_id`),
  KEY `idx_books_publisher_id` (`publisher_id`),
  KEY `idx_books_supplier_id` (`supplier_id`),
  CONSTRAINT `FKayy5edfrqnegqj3882nce6qo8` FOREIGN KEY (`publisher_id`) REFERENCES `publishers` (`id`),
  CONSTRAINT `FKejyaj8fotqvtajvkdo7ocdvw3` FOREIGN KEY (`supplier_id`) REFERENCES `suppliers` (`id`),
  CONSTRAINT `FKleqa3hhc0uhfvurq6mil47xk0` FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `books`
--

LOCK TABLES `books` WRITE;
/*!40000 ALTER TABLE `books` DISABLE KEYS */;
INSERT INTO `books` VALUES (1,'PAPERBACK','2026-04-03 20:54:08.000000','system','Tác phẩm nổi tiếng của Nguyễn Nhật Ánh, kể về hành trình quay ngược thời gian để tìm lại tuổi thơ đã mất. Một câu chuyện đầy hoài niệm và xúc động về tình bạn, tuổi thơ và những ước mơ giản dị.','13 x 20 cm',10,'1775225121726-0-cho_toi_xin_mot_1.png',220,85000,2008,144,1206,'Cho tôi xin một vé đi tuổi thơ','2026-04-19 03:11:31.514658','anonymousUser',255,1,1,1),(2,'PAPERBACK','2026-04-03 20:54:08.000000','system','Câu chuyện về hai anh em Thiều và Tường lớn lên ở một làng quê miền Trung. Cuốn sách đã được chuyển thể thành phim điện ảnh cùng tên, gây tiếng vang lớn năm 2015.','13 x 20 cm',15,'1775225313589-1-toi_thay_hoa_vang_tren_co_2.png',378,95000,2010,194,1806,'Tôi thấy hoa vàng trên cỏ xanh','2026-04-19 03:11:31.514658','anonymousUser',300,1,1,1),(3,'PAPERBACK','2026-04-03 20:54:08.000000','system','Tiểu thuyết trào phúng xuất sắc nhất của văn học Việt Nam hiện đại. Qua nhân vật Xuân Tóc Đỏ, Vũ Trọng Phụng phơi bày xã hội thượng lưu Hà Nội những năm 1930 với giọng văn châm biếm sắc sảo.','14.5 x 20.5 cm',5,'1775225489735-0-so_do_1.png',280,75000,2020,100,950,'Số đỏ','2026-04-03 14:11:29.774336','admin@bookverse.com',280,1,2,2),(4,'PAPERBACK','2026-04-03 20:54:08.000000','system','Tác phẩm hiện thực phê phán tiêu biểu của văn học Việt Nam. Qua nhân vật chị Dậu, Ngô Tất Tố lên án chế độ sưu thuế tàn bạo dưới thời thực dân phong kiến.','13 x 20 cm',5,'1775225602664-0-tat_den_1.png',188,68000,2019,98,892,'Tắt đèn','2026-04-18 11:35:15.068667','customer1@bookverse.com',220,1,2,2),(5,'PAPERBACK','2026-04-03 20:54:08.000000','system','Tiểu thuyết bán chạy nhất mọi thời đại của Paulo Coelho. Câu chuyện về chàng chăn cừu Santiago trong hành trình theo đuổi giấc mơ đến Kim Tự Tháp Ai Cập, với thông điệp: khi bạn thực sự khao khát điều gì, cả vũ trụ sẽ hợp lực giúp bạn.','13 x 20 cm',20,'1775225707938-0-nha_gia_kim_1.png',228,69000,2020,289,2511,'Nhà giả kim','2026-04-18 11:35:15.068667','customer1@bookverse.com',200,1,2,1),(6,'PAPERBACK','2026-04-03 20:54:41.000000','system','Best-seller vượt thời gian của Dale Carnegie về nghệ thuật đối nhân xử thế. Sách hướng dẫn cách giao tiếp, thuyết phục và tạo ảnh hưởng với người khác, đã bán hơn 30 triệu bản trên toàn thế giới.','14.5 x 20.5 cm',25,'1775226192818-0-dac_nhan_tam_1.png',320,86000,2016,500,5000,'Đắc nhân tâm','2026-04-03 14:23:12.853159','admin@bookverse.com',350,2,3,3),(7,'PAPERBACK','2026-04-03 20:54:41.000000','system','Tác phẩm kinh điển của Napoleon Hill, đúc kết từ 20 năm nghiên cứu 500 người giàu nhất nước Mỹ. Sách chỉ ra 13 nguyên tắc vàng để đạt được thành công và giàu có.','14.5 x 20.5 cm',15,'1775226295209-0-nghi_giau_lam_giau_1.png',400,110000,2018,180,1600,'Nghĩ giàu làm giàu','2026-04-03 14:24:55.238857','admin@bookverse.com',380,2,3,3),(8,'PAPERBACK','2026-04-03 20:54:41.000000','system','Cuốn sách tài chính cá nhân bán chạy nhất mọi thời đại. Robert Kiyosaki chia sẻ những bài học về tiền bạc từ hai người cha: cha ruột nghèo và cha của bạn thân giàu có.','14 x 20.5 cm',20,'1775226455927-0-cha_giau_cha_ngheo_1.png',370,125000,2019,250,3200,'Cha giàu cha nghèo','2026-04-03 14:27:35.964079','admin@bookverse.com',350,2,1,1),(9,'PAPERBACK','2026-04-03 20:54:41.000000','system','Timothy Ferriss chia sẻ cách thoát khỏi vòng quay 9-to-5, tự động hóa thu nhập và sống cuộc đời tự do. Phương pháp DEAL: Definition, Elimination, Automation, Liberation.','15 x 22 cm',10,'1775226564695-0-tuan_lam_viec_4_gio_1.png',416,135000,2020,90,680,'Tuần làm việc 4 giờ','2026-04-03 14:29:24.726478','admin@bookverse.com',400,2,4,3),(10,'PAPERBACK','2026-04-03 20:54:41.000000','system','Tác phẩm kinh điển của George S. Clason về các nguyên tắc quản lý tài chính cá nhân thông qua những câu chuyện ngụ ngôn từ thành Babylon cổ đại.','13 x 20 cm',10,'1775227358679-0-nguoi_giau_co_nhat_thanh_1.png',208,79000,2017,160,1400,'Người giàu có nhất thành Babylon','2026-04-03 14:42:38.708383','admin@bookverse.com',220,2,3,1),(11,'PAPERBACK','2026-04-03 20:55:20.000000','system','Truyện dài nổi tiếng của Tô Hoài kể về cuộc phiêu lưu của chú Dế Mèn, từ một kẻ kiêu căng trở thành người dũng cảm, nhân hậu. Tác phẩm thiếu nhi kinh điển của văn học Việt Nam.','13 x 19 cm',10,'1776500316788-0-de_men_phieu_luu_ky_1.png',168,55000,2018,300,2200,'Dế Mèn phiêu lưu ký','2026-04-18 08:26:23.851715','admin@bookverse.com',180,3,5,1),(12,'PAPERBACK','2026-04-03 20:55:20.000000','system','Bộ truyện dài nổi tiếng của Nguyễn Nhật Ánh kể về nhóm bạn nhỏ với những trò nghịch ngợm vui nhộn, bài học về tình bạn và tuổi thơ đầy màu sắc.','13 x 19 cm',10,'1776500562509-0-kinh_van_hoa_1.png',200,65000,2015,180,1500,'Kính vạn hoa','2026-04-18 08:26:26.601702','admin@bookverse.com',220,3,5,2),(13,'HARDCOVER','2026-04-03 20:55:20.000000','system','Kiệt tác văn học của Antoine de Saint-Exupéry. Câu chuyện về hoàng tử nhỏ từ tiểu hành tinh B612, với những bài học sâu sắc về tình yêu, tình bạn và ý nghĩa cuộc sống.','15 x 21 cm',15,'1776500668066-0-hoang_tu_be_1.png',112,52000,2019,250,3000,'Hoàng tử bé','2026-04-18 08:26:29.999461','admin@bookverse.com',150,3,5,1),(14,'PAPERBACK','2026-04-03 20:55:20.000000','system','Hồi ký tuổi thơ của Tetsuko Kuroyanagi tại ngôi trường Tomoe đặc biệt. Cuốn sách thiếu nhi bán chạy nhất trong lịch sử Nhật Bản, đề cao phương pháp giáo dục tôn trọng cá tính trẻ.','13 x 20 cm',10,'1776500765012-0-totto_chan_ben_cua_so_1.png',248,78000,2016,200,1800,'Totto-chan bên cửa sổ','2026-04-18 08:26:33.509298','admin@bookverse.com',250,3,5,4),(15,'PAPERBACK','2026-04-03 20:55:20.000000','system','Truyện dài của Nguyễn Nhật Ánh về cậu bé Quý ròm với những tình huống dở khóc dở cười ở trường học và gia đình, mang đến tiếng cười và cảm xúc cho độc giả nhỏ tuổi.','13 x 19 cm',5,'1776500914359-0-chu_be_rac_roi_1.png',192,60000,2017,140,900,'Chú bé rắc rối','2026-04-18 08:28:34.384568','admin@bookverse.com',200,3,5,2),(16,'PAPERBACK','2026-04-03 20:55:58.000000','system','Tác phẩm kinh điển của Stephen R. Covey về phát triển bản thân và lãnh đạo. 7 nguyên tắc từ chủ động, bắt đầu từ mục tiêu, ưu tiên việc quan trọng đến tư duy cùng thắng và hiệp lực.','15.5 x 23 cm',15,'1776501014076-0-7_thoi_quen_hieu_qua_1.png',480,159000,2019,169,2101,'7 thói quen hiệu quả','2026-04-18 11:40:32.745377','customer1@bookverse.com',450,4,1,3),(17,'PAPERBACK','2026-04-03 20:55:58.000000','system','Tiến sĩ David J. Lieberman hướng dẫn cách đọc vị tâm lý, nhận biết nói dối và hiểu được suy nghĩ của người khác thông qua ngôn ngữ cơ thể và hành vi.','13 x 20 cm',10,'1776501589882-0-doc_vi_bat_ki_ai_1.png',240,89000,2018,129,1101,'Đọc vị bất kỳ ai','2026-04-18 11:40:32.745377','customer1@bookverse.com',280,4,4,4),(18,'PAPERBACK','2026-04-03 20:55:58.000000','system','Charles Duhigg giải thích khoa học đằng sau thói quen — cách chúng hình thành, hoạt động và thay đổi như thế nào. Áp dụng vào cá nhân, tổ chức và xã hội.','14.5 x 20.5 cm',10,'1776501730014-0-suc_manh_cua_thoi_quen_1.png',368,129000,2020,150,1350,'Sức mạnh của thói quen','2026-04-18 08:42:10.043370','admin@bookverse.com',380,4,4,3),(19,'PAPERBACK','2026-04-03 20:55:58.000000','system','Tác phẩm đoạt giải Nobel của Daniel Kahneman về hai hệ thống tư duy: Hệ thống 1 (nhanh, trực giác) và Hệ thống 2 (chậm, lý trí). Cuốn sách thay đổi cách bạn nghĩ về tư duy.','15.5 x 23 cm',5,'1776501814881-0-tu_duy_nhanh_va_cham_1.png',600,199000,2021,98,752,'Tư duy nhanh và chậm','2026-04-19 02:53:42.065270','customer1@bookverse.com',550,4,6,3),(20,'PAPERBACK','2026-04-03 20:55:58.000000','system','James Clear trình bày hệ thống 4 bước xây dựng thói quen tốt và loại bỏ thói quen xấu. Chỉ cần thay đổi 1% mỗi ngày, bạn sẽ đạt kết quả phi thường theo thời gian.','14.5 x 20.5 cm',20,'1776501932952-0-thay_doi_ti_hon_1.png',320,149000,2022,277,3503,'Atomic Habits - Thay đổi tí hon hiệu quả bất ngờ','2026-04-19 02:53:42.065270','customer1@bookverse.com',350,4,6,4);
/*!40000 ALTER TABLE `books` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cart_details`
--

DROP TABLE IF EXISTS `cart_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cart_details` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `price` double NOT NULL,
  `quantity` bigint NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `updated_by` varchar(255) DEFAULT NULL,
  `book_id` bigint DEFAULT NULL,
  `cart_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_cart_details_cart_id` (`cart_id`),
  KEY `idx_cart_details_book_id` (`book_id`),
  KEY `idx_cart_details_cart_book` (`cart_id`,`book_id`),
  CONSTRAINT `FK3jjrdv6317gvtwdq2vt4vhh3i` FOREIGN KEY (`book_id`) REFERENCES `books` (`id`),
  CONSTRAINT `FKkcochhsa891wv0s9wrtf36wgt` FOREIGN KEY (`cart_id`) REFERENCES `carts` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cart_details`
--

LOCK TABLES `cart_details` WRITE;
/*!40000 ALTER TABLE `cart_details` DISABLE KEYS */;
INSERT INTO `cart_details` VALUES (4,'2026-04-08 13:14:09.400190','customer2@bookverse.com',64500,1,NULL,NULL,6,2),(5,'2026-04-08 13:14:09.844583','customer2@bookverse.com',93500,1,NULL,NULL,7,2);
/*!40000 ALTER TABLE `cart_details` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `carts`
--

DROP TABLE IF EXISTS `carts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `carts` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `sum` int NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `updated_by` varchar(255) DEFAULT NULL,
  `customer_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK88sv4i13lo80s74ox7rsb5a2c` (`customer_id`),
  KEY `idx_carts_customer_id` (`customer_id`),
  CONSTRAINT `FK8ba3sryid5k8a9kidpkvqipyt` FOREIGN KEY (`customer_id`) REFERENCES `customers` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `carts`
--

LOCK TABLES `carts` WRITE;
/*!40000 ALTER TABLE `carts` DISABLE KEYS */;
INSERT INTO `carts` VALUES (1,'2026-04-03 08:33:17.058865','',0,'2026-04-19 03:11:03.365370','customer1@bookverse.com',1),(2,'2026-04-03 08:33:17.148760','',2,'2026-04-08 13:14:09.853025','customer2@bookverse.com',2),(3,'2026-04-03 08:33:17.236448','',0,NULL,NULL,3),(4,'2026-04-03 08:33:17.320176','',0,NULL,NULL,4),(5,'2026-04-03 08:33:17.405183','',0,NULL,NULL,5),(6,'2026-04-03 08:33:17.493223','',0,NULL,NULL,6),(7,'2026-04-03 08:33:17.578067','',0,NULL,NULL,7),(8,'2026-04-03 08:33:17.659859','',0,NULL,NULL,8),(9,'2026-04-03 08:33:17.760068','',0,NULL,NULL,9),(10,'2026-04-03 08:33:17.871930','',0,NULL,NULL,10);
/*!40000 ALTER TABLE `carts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `categories`
--

DROP TABLE IF EXISTS `categories`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `categories` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `description` mediumtext,
  `name` varchar(255) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `updated_by` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_categories_name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `categories`
--

LOCK TABLES `categories` WRITE;
/*!40000 ALTER TABLE `categories` DISABLE KEYS */;
INSERT INTO `categories` VALUES (1,'2026-04-03 20:52:37.000000','system','Sách văn học Việt Nam và thế giới, tiểu thuyết, truyện ngắn, thơ ca','Văn học',NULL,NULL),(2,'2026-04-03 20:52:37.000000','system','Sách kinh tế, tài chính, quản trị kinh doanh, đầu tư','Kinh tế',NULL,NULL),(3,'2026-04-03 20:52:37.000000','system','Sách dành cho thiếu nhi, truyện tranh, truyện cổ tích, sách giáo dục trẻ em','Thiếu nhi',NULL,NULL),(4,'2026-04-03 20:52:37.000000','system','Sách kỹ năng sống, phát triển bản thân, tư duy, giao tiếp','Kỹ năng',NULL,NULL);
/*!40000 ALTER TABLE `categories` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `customers`
--

DROP TABLE IF EXISTS `customers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `customers` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `customer_level` enum('BRONZE','DIAMOND','GOLD','SILVER') DEFAULT NULL,
  `identity_card` varchar(255) DEFAULT NULL,
  `total_order` bigint DEFAULT NULL,
  `total_spending` decimal(38,2) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `updated_by` varchar(255) DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKeuat1oase6eqv195jvb71a93s` (`user_id`),
  KEY `idx_customers_identity_card` (`identity_card`),
  KEY `idx_customers_created_at` (`created_at`),
  KEY `idx_customers_user_id` (`user_id`),
  CONSTRAINT `FKrh1g1a20omjmn6kurd35o3eit` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customers`
--

LOCK TABLES `customers` WRITE;
/*!40000 ALTER TABLE `customers` DISABLE KEYS */;
INSERT INTO `customers` VALUES (1,'2026-04-03 08:33:17.048122','','SILVER','079200001001',2,1251750.00,'2026-04-19 03:11:31.514658','anonymousUser',9),(2,'2026-04-03 08:33:17.139148','','BRONZE','079200001002',0,0.00,NULL,NULL,10),(3,'2026-04-03 08:33:17.227574','','BRONZE','079200001003',0,0.00,NULL,NULL,11),(4,'2026-04-03 08:33:17.313964','','BRONZE','079200001004',0,0.00,NULL,NULL,12),(5,'2026-04-03 08:33:17.398246','','BRONZE','079200001005',0,0.00,NULL,NULL,13),(6,'2026-04-03 08:33:17.483618','','BRONZE','079200001006',0,0.00,NULL,NULL,14),(7,'2026-04-03 08:33:17.568795','','BRONZE','079200001007',0,0.00,NULL,NULL,15),(8,'2026-04-03 08:33:17.653570','','BRONZE','079200001008',0,0.00,NULL,NULL,16),(9,'2026-04-03 08:33:17.749028','','BRONZE','079200001009',0,0.00,NULL,NULL,17),(10,'2026-04-03 08:33:17.863990','','BRONZE','079200001010',0,0.00,NULL,NULL,18);
/*!40000 ALTER TABLE `customers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_details`
--

DROP TABLE IF EXISTS `order_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order_details` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `price` double NOT NULL,
  `quantity` bigint NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `updated_by` varchar(255) DEFAULT NULL,
  `book_id` bigint DEFAULT NULL,
  `order_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_order_details_order_id` (`order_id`),
  KEY `idx_order_details_book_id` (`book_id`),
  KEY `idx_order_details_book_order` (`book_id`,`order_id`),
  CONSTRAINT `FKjqe04yonp6a52rhbf2y0m03qw` FOREIGN KEY (`book_id`) REFERENCES `books` (`id`),
  CONSTRAINT `FKjyu2qbqt8gnvno9oe9j2s2ldk` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_details`
--

LOCK TABLES `order_details` WRITE;
/*!40000 ALTER TABLE `order_details` DISABLE KEYS */;
INSERT INTO `order_details` VALUES (1,'2026-04-19 02:53:42.051965','customer1@bookverse.com',119200,1,NULL,NULL,20,35),(2,'2026-04-19 02:53:42.054539','customer1@bookverse.com',189050,1,NULL,NULL,19,35),(3,'2026-04-19 03:11:03.334798','customer1@bookverse.com',76500,6,NULL,NULL,1,36),(4,'2026-04-19 03:11:03.337326','customer1@bookverse.com',80750,6,NULL,NULL,2,36);
/*!40000 ALTER TABLE `order_details` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_payments`
--

DROP TABLE IF EXISTS `order_payments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order_payments` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `amount` double NOT NULL,
  `completed_at` datetime(6) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `gateway_response_code` varchar(32) DEFAULT NULL,
  `gateway_transaction_id` varchar(128) DEFAULT NULL,
  `method` enum('COD','VNPAY') NOT NULL,
  `provider_ref` varchar(64) NOT NULL,
  `raw_payload` text,
  `status` enum('CANCELLED','FAILED','INITIATED','SUCCESS') NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `updated_by` varchar(255) DEFAULT NULL,
  `order_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKgfjvfmk8c8pkuoonkf09kw9w2` (`provider_ref`),
  KEY `FK3s9vxneb3dk3plhpv9s213so0` (`order_id`),
  CONSTRAINT `FK3s9vxneb3dk3plhpv9s213so0` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_payments`
--

LOCK TABLES `order_payments` WRITE;
/*!40000 ALTER TABLE `order_payments` DISABLE KEYS */;
INSERT INTO `order_payments` VALUES (1,943500,'2026-04-19 03:11:26.857333','2026-04-19 03:11:03.350714','customer1@bookverse.com','00','15502896','VNPAY','BV36T1776568263349','{vnp_Amount=94350000, vnp_BankCode=NCB, vnp_BankTranNo=VNP15502896, vnp_CardType=ATM, vnp_OrderInfo=Thanh toan don hang BV-7f804c9ac8cf418e, vnp_PayDate=20260419101122, vnp_ResponseCode=00, vnp_TmnCode=PDKGEDOW, vnp_TransactionNo=15502896, vnp_TransactionStatus=00, vnp_TxnRef=BV36T1776568263349, vnp_SecureHash=49d25d2f5c3f484473a8da29366dfcc770d47fb4a6f535e9ee1fc39ca1f9c3aaf64e9f38e8592d0626990b8e475f03a71a358ab61b1eca84e399641b9749b48d}','SUCCESS','2026-04-19 03:11:31.514658','anonymousUser',36);
/*!40000 ALTER TABLE `order_payments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orders` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `receiver_address` varchar(255) DEFAULT NULL,
  `receiver_email` varchar(255) DEFAULT NULL,
  `receiver_name` varchar(255) DEFAULT NULL,
  `receiver_phone` varchar(255) DEFAULT NULL,
  `status` enum('CANCELLED','CONFIRMED','DELIVERED','PENDING','SHIPPING') DEFAULT NULL,
  `total_price` double NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `updated_by` varchar(255) DEFAULT NULL,
  `customer_id` bigint DEFAULT NULL,
  `discount_total` double NOT NULL,
  `order_code` varchar(40) DEFAULT NULL,
  `paid_at` datetime(6) DEFAULT NULL,
  `payment_method` enum('COD','VNPAY') DEFAULT NULL,
  `payment_status` enum('FAILED','PAID','PENDING','REFUNDED') DEFAULT NULL,
  `shipping_fee` double NOT NULL,
  `subtotal` double NOT NULL,
  `note` mediumtext,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKdhk2umg8ijjkg4njg6891trit` (`order_code`),
  KEY `idx_orders_customer_id` (`customer_id`),
  KEY `idx_orders_created_at` (`created_at`),
  KEY `idx_orders_status_created_at` (`status`,`created_at`),
  KEY `idx_orders_payment_created_at` (`payment_method`,`payment_status`,`created_at`),
  CONSTRAINT `FKpxtb8awmi0dk6smoh2vp1litg` FOREIGN KEY (`customer_id`) REFERENCES `customers` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders`
--

LOCK TABLES `orders` WRITE;
/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
INSERT INTO `orders` VALUES (35,'2026-04-19 02:53:42.046867','customer1@bookverse.com','27 Phong Phu, Phong Phu, Binh Chanh, Ho Chi Minh','customer1@bookverse.com','Bùi Văn Khách','0932979223','DELIVERED',308250,'2026-04-21 04:23:28.864801','admin@bookverse.com',1,39750,'BV-c1df7d478a3f4a5f','2026-04-19 03:08:45.158153','COD','PAID',0,348000,'Giao vao thu 7 chu nhat'),(36,'2026-04-19 03:11:03.331289','customer1@bookverse.com','27 Phong Phu, Phong Phu, Binh Chanh, Ho Chi Minh','customer1@bookverse.com','Bùi Văn Khách','0932979223','SHIPPING',943500,'2026-04-21 04:07:40.650927','admin@bookverse.com',1,136500,'BV-7f804c9ac8cf418e','2026-04-19 03:11:26.857333','VNPAY','PAID',0,1080000,'');
/*!40000 ALTER TABLE `orders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `permission_role`
--

DROP TABLE IF EXISTS `permission_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `permission_role` (
  `role_id` bigint NOT NULL,
  `permission_id` bigint NOT NULL,
  KEY `idx_permission_role_role_id` (`role_id`),
  KEY `idx_permission_role_permission_id` (`permission_id`),
  CONSTRAINT `FK3vhflqw0lwbwn49xqoivrtugt` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`),
  CONSTRAINT `FK6mg4g9rc8u87l0yavf8kjut05` FOREIGN KEY (`permission_id`) REFERENCES `permissions` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `permission_role`
--

LOCK TABLES `permission_role` WRITE;
/*!40000 ALTER TABLE `permission_role` DISABLE KEYS */;
INSERT INTO `permission_role` VALUES (2,7),(2,8),(2,9),(2,10),(2,11),(2,1),(2,2),(2,3),(2,4),(2,5),(2,6),(2,39),(2,40),(2,41),(2,42),(2,43),(2,14),(2,15),(2,16),(2,17),(2,18),(2,50),(2,51),(2,52),(2,53),(2,54),(2,22),(2,23),(2,28),(2,29),(2,26),(2,24),(3,10),(3,11),(3,4),(3,5),(3,6),(3,42),(3,43),(3,17),(3,18),(3,53),(3,54),(3,19),(3,20),(3,21),(3,22),(3,23),(3,25),(3,26),(3,27),(3,28),(3,29),(3,24),(4,12),(4,13),(4,25),(4,29),(4,61),(1,1),(1,2),(1,3),(1,4),(1,5),(1,6),(1,7),(1,8),(1,9),(1,10),(1,11),(1,12),(1,13),(1,14),(1,15),(1,16),(1,17),(1,18),(1,19),(1,20),(1,21),(1,22),(1,23),(1,24),(1,25),(1,26),(1,27),(1,28),(1,29),(1,33),(1,34),(1,35),(1,36),(1,37),(1,38),(1,39),(1,40),(1,41),(1,42),(1,43),(1,44),(1,45),(1,46),(1,47),(1,48),(1,49),(1,50),(1,51),(1,52),(1,53),(1,54),(1,55),(1,56),(1,57),(1,58),(1,59),(1,60),(1,61),(1,62);
/*!40000 ALTER TABLE `permission_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `permissions`
--

DROP TABLE IF EXISTS `permissions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `permissions` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `api_path` varchar(255) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `domain` varchar(255) NOT NULL,
  `method` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `updated_by` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_permissions_name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=63 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `permissions`
--

LOCK TABLES `permissions` WRITE;
/*!40000 ALTER TABLE `permissions` DISABLE KEYS */;
INSERT INTO `permissions` VALUES (1,'/api/v1/authors','2026-04-03 08:33:15.809649','','AUTHOR','POST','AUTHOR_CREATE',NULL,NULL),(2,'/api/v1/authors','2026-04-03 08:33:15.841092','','AUTHOR','PUT','AUTHOR_UPDATE',NULL,NULL),(3,'/api/v1/authors/{id}','2026-04-03 08:33:15.845129','','AUTHOR','DELETE','AUTHOR_DELETE',NULL,NULL),(4,'/api/v1/authors','2026-04-03 08:33:15.846896','','AUTHOR','GET','AUTHOR_VIEW_ALL',NULL,NULL),(5,'/api/v1/authors/{id}','2026-04-03 08:33:15.846896','','AUTHOR','GET','AUTHOR_VIEW_BY_ID',NULL,NULL),(6,'/api/v1/authors/search','2026-04-03 08:33:15.846896','','AUTHOR','GET','AUTHOR_VIEW_ALL_WITH_PAGINATION_AND_FILTER',NULL,NULL),(7,'/api/v1/books','2026-04-03 08:33:15.846896','','BOOK','POST','BOOK_CREATE',NULL,NULL),(8,'/api/v1/books','2026-04-03 08:33:15.852496','','BOOK','PUT','BOOK_UPDATE',NULL,NULL),(9,'/api/v1/books/{id}','2026-04-03 08:33:15.855323','','BOOK','DELETE','BOOK_DELETE',NULL,NULL),(10,'/api/v1/books','2026-04-03 08:33:15.855323','','BOOK','GET','BOOK_VIEW_ALL',NULL,NULL),(11,'/api/v1/books/search','2026-04-03 08:33:15.857340','','BOOK','GET','BOOK_VIEW_ALL_WITH_PAGINATION_AND_FILTER',NULL,NULL),(12,'/api/v1/carts/items','2026-04-03 08:33:15.857340','','CART','POST','CART_ADD_TO_CART',NULL,NULL),(13,'/api/v1/carts','2026-04-03 08:33:15.860979','','CART','GET','CART_VIEW_BY_ID',NULL,NULL),(14,'/api/v1/categories','2026-04-03 08:33:15.862941','','CATEGORY','POST','CATEGORY_CREATE',NULL,NULL),(15,'/api/v1/categories','2026-04-03 08:33:15.864478','','CATEGORY','PUT','CATEGORY_UPDATE',NULL,NULL),(16,'/api/v1/categories/{id}','2026-04-03 08:33:15.865903','','CATEGORY','DELETE','CATEGORY_DELETE',NULL,NULL),(17,'/api/v1/categories/{id}','2026-04-03 08:33:15.867465','','CATEGORY','GET','CATEGORY_VIEW_BY_ID',NULL,NULL),(18,'/api/v1/categories/search','2026-04-03 08:33:15.869688','','CATEGORY','GET','CATEGORY_VIEW_ALL_WITH_PAGINATION_AND_FILTER',NULL,NULL),(19,'/api/v1/customers','2026-04-03 08:33:15.870717','','CUSTOMER','POST','CUSTOMER_CREATE',NULL,NULL),(20,'/api/v1/customers','2026-04-03 08:33:15.871715','','CUSTOMER','PUT','CUSTOMER_UPDATE',NULL,NULL),(21,'/api/v1/customers/{id}','2026-04-03 08:33:15.871715','','CUSTOMER','DELETE','CUSTOMER_DELETE',NULL,NULL),(22,'/api/v1/customers/{id}','2026-04-03 08:33:15.876578','','CUSTOMER','GET','CUSTOMER_VIEW_BY_ID',NULL,NULL),(23,'/api/v1/customers/search','2026-04-03 08:33:15.877971','','CUSTOMER','GET','CUSTOMER_VIEW_ALL_WITH_PAGINATION_AND_FILTER',NULL,NULL),(24,'/api/v1/files','2026-04-03 08:33:15.879508','','FILE','POST','FILE_UPLOAD',NULL,NULL),(25,'/api/v1/orders','2026-04-03 08:33:15.881022','','ORDER','POST','ORDER_CREATE',NULL,NULL),(26,'/api/v1/orders','2026-04-03 08:33:15.882743','','ORDER','PUT','ORDER_UPDATE',NULL,NULL),(27,'/api/v1/orders/{id}','2026-04-03 08:33:15.882743','','ORDER','DELETE','ORDER_CANCEL','2026-04-08 07:26:18.712165','admin@bookverse.com'),(28,'/api/v1/orders','2026-04-03 08:33:15.885395','','ORDER','GET','ORDER_VIEW_ALL_WITH_PAGINATION_AND_FILTER','2026-04-08 14:22:45.035881','admin@bookverse.com'),(29,'/api/v1/orders/{id}','2026-04-03 08:33:15.888210','','ORDER','GET','ORDER_VIEW_BY_ID',NULL,NULL),(33,'/api/v1/permissions','2026-04-03 08:33:15.895270','','PERMISSION','POST','PERMISSION_CREATE',NULL,NULL),(34,'/api/v1/permissions','2026-04-03 08:33:15.895270','','PERMISSION','PUT','PERMISSION_UPDATE',NULL,NULL),(35,'/api/v1/permissions/{id}','2026-04-03 08:33:15.902049','','PERMISSION','DELETE','PERMISSION_DELETE',NULL,NULL),(36,'/api/v1/permissions','2026-04-03 08:33:15.902560','','PERMISSION','GET','PERMISSION_VIEW_ALL',NULL,NULL),(37,'/api/v1/permissions/{id}','2026-04-03 08:33:15.905194','','PERMISSION','GET','PERMISSION_VIEW_BY_ID',NULL,NULL),(38,'/api/v1/permissions/search','2026-04-03 08:33:15.907836','','PERMISSION','GET','PERMISSION_VIEW_ALL_WITH_PAGINATION_AND_FILTER',NULL,NULL),(39,'/api/v1/publishers','2026-04-03 08:33:15.909844','','PUBLISHER','POST','PUBLISHER_CREATE',NULL,NULL),(40,'/api/v1/publishers','2026-04-03 08:33:15.911439','','PUBLISHER','PUT','PUBLISHER_UPDATE',NULL,NULL),(41,'/api/v1/publishers/{id}','2026-04-03 08:33:15.913453','','PUBLISHER','DELETE','PUBLISHER_DELETE',NULL,NULL),(42,'/api/v1/publishers/{id}','2026-04-03 08:33:15.915461','','PUBLISHER','GET','PUBLISHER_VIEW_BY_ID',NULL,NULL),(43,'/api/v1/publishers/search','2026-04-03 08:33:15.915461','','PUBLISHER','GET','PUBLISHER_VIEW_ALL_WITH_PAGINATION_AND_FILTER',NULL,NULL),(44,'/api/v1/roles','2026-04-03 08:33:15.918530','','ROLE','POST','ROLE_CREATE',NULL,NULL),(45,'/api/v1/roles','2026-04-03 08:33:15.920528','','ROLE','PUT','ROLE_UPDATE',NULL,NULL),(46,'/api/v1/roles/{id}','2026-04-03 08:33:15.921530','','ROLE','DELETE','ROLE_DELETE',NULL,NULL),(47,'/api/v1/roles','2026-04-03 08:33:15.923531','','ROLE','GET','ROLE_VIEW_ALL',NULL,NULL),(48,'/api/v1/roles/{id}','2026-04-03 08:33:15.925145','','ROLE','GET','ROLE_VIEW_BY_ID',NULL,NULL),(49,'/api/v1/roles/search','2026-04-03 08:33:15.926657','','ROLE','GET','ROLE_VIEW_ALL_WITH_PAGINATION_AND_FILTER',NULL,NULL),(50,'/api/v1/suppliers','2026-04-03 08:33:15.928666','','SUPPLIER','POST','SUPPLIER_CREATE',NULL,NULL),(51,'/api/v1/suppliers','2026-04-03 08:33:15.930021','','SUPPLIER','PUT','SUPPLIER_UPDATE',NULL,NULL),(52,'/api/v1/suppliers/{id}','2026-04-03 08:33:15.931235','','SUPPLIER','DELETE','SUPPLIER_DELETE',NULL,NULL),(53,'/api/v1/suppliers/{id}','2026-04-03 08:33:15.933259','','SUPPLIER','GET','SUPPLIER_VIEW_BY_ID',NULL,NULL),(54,'/api/v1/suppliers/search','2026-04-03 08:33:15.935691','','SUPPLIER','GET','SUPPLIER_VIEW_ALL_WITH_PAGINATION_AND_FILTER',NULL,NULL),(55,'/api/v1/users','2026-04-03 08:33:15.937711','','USER','POST','USER_CREATE',NULL,NULL),(56,'/api/v1/users','2026-04-03 08:33:15.939352','','USER','PUT','USER_UPDATE',NULL,NULL),(57,'/api/v1/users/{id}','2026-04-03 08:33:15.939352','','USER','DELETE','USER_DELETE',NULL,NULL),(58,'/api/v1/users','2026-04-03 08:33:15.942783','','USER','GET','USER_VIEW_ALL',NULL,NULL),(59,'/api/v1/users/{id}','2026-04-03 08:33:15.943773','','USER','GET','USER_VIEW_BY_ID',NULL,NULL),(60,'/api/v1/users/search','2026-04-03 08:33:15.945258','','USER','GET','USER_VIEW_ALL_WITH_PAGINATION_AND_FILTER',NULL,NULL),(61,'/api/v1/orders/me','2026-04-08 07:24:59.309766','admin@bookverse.com','ORDER','GET','ORDER_VIEW_MINE',NULL,NULL),(62,'/api/v1/dashboard/overview','2026-04-21 04:06:08.728387','admin@bookverse.com','DASHBOARD','GET','DASHBOARD_VIEW',NULL,NULL);
/*!40000 ALTER TABLE `permissions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `publishers`
--

DROP TABLE IF EXISTS `publishers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `publishers` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `address` varchar(255) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `description` mediumtext,
  `email` varchar(255) DEFAULT NULL,
  `image` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `updated_by` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_publishers_name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `publishers`
--

LOCK TABLES `publishers` WRITE;
/*!40000 ALTER TABLE `publishers` DISABLE KEYS */;
INSERT INTO `publishers` VALUES (1,'161B Lý Chính Thắng, Phường Võ Thị Sáu, Quận 3, TP.HCM','2026-04-03 20:52:59.000000','system','Nhà xuất bản Trẻ — một trong những NXB lớn nhất Việt Nam','hopthubandoc@nxbtre.com.vn',NULL,'NXB Trẻ','02839316289',NULL,NULL),(2,'65 Nguyễn Du, Hai Bà Trưng, Hà Nội','2026-04-03 20:52:59.000000','system','Nhà xuất bản Hội Nhà Văn Việt Nam','nxbhoinhavanvn@gmail.com',NULL,'NXB Hội Nhà Văn','02438222135',NULL,NULL),(3,'62 Nguyễn Thị Minh Khai, Quận 1, TP.HCM','2026-04-03 20:52:59.000000','system','Nhà xuất bản Tổng hợp Thành phố Hồ Chí Minh','tonghop@nxbhcm.com.vn',NULL,'NXB Tổng hợp TP.HCM','02838225340',NULL,NULL),(4,'175 Giảng Võ, Đống Đa, Hà Nội','2026-04-03 20:52:59.000000','system','Nhà xuất bản Lao Động','nxblaodong@gmail.com',NULL,'NXB Lao Động','02438515380',NULL,NULL),(5,'55 Quang Trung, Hai Bà Trưng, Hà Nội','2026-04-03 20:52:59.000000','system','Nhà xuất bản Kim Đồng — chuyên sách thiếu nhi','cskh@nxbkimdong.com.vn',NULL,'NXB Kim Đồng','02439434730',NULL,NULL),(6,'46 Trần Hưng Đạo, Hoàn Kiếm, Hà Nội','2026-04-03 20:52:59.000000','system','Nhà xuất bản Thế Giới','nxbthegioi@gmail.com',NULL,'NXB Thế Giới','02438253841',NULL,NULL);
/*!40000 ALTER TABLE `publishers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `roles`
--

DROP TABLE IF EXISTS `roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `roles` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `updated_by` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_roles_name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `roles`
--

LOCK TABLES `roles` WRITE;
/*!40000 ALTER TABLE `roles` DISABLE KEYS */;
INSERT INTO `roles` VALUES (1,'2026-04-03 08:33:16.002928','','Quản trị viên hệ thống — toàn quyền','ADMIN','2026-04-21 04:06:22.582296','admin@bookverse.com'),(2,'2026-04-03 08:33:16.182939','','Quản lý — quản lý sản phẩm, xem đơn hàng và khách hàng','MANAGER',NULL,NULL),(3,'2026-04-03 08:33:16.278850','','Nhân viên — quản lý khách hàng và đơn hàng','STAFF',NULL,NULL),(4,'2026-04-03 08:33:16.310137','','Khách hàng — mua sắm, giỏ hàng, đặt hàng','CUSTOMER','2026-04-08 07:25:10.862791','admin@bookverse.com');
/*!40000 ALTER TABLE `roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `suppliers`
--

DROP TABLE IF EXISTS `suppliers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `suppliers` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `address` varchar(255) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `description` mediumtext,
  `email` varchar(255) DEFAULT NULL,
  `image` varchar(255) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `updated_by` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_suppliers_name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `suppliers`
--

LOCK TABLES `suppliers` WRITE;
/*!40000 ALTER TABLE `suppliers` DISABLE KEYS */;
INSERT INTO `suppliers` VALUES (1,'60-62 Lê Lợi, Quận 1, TP.HCM','2026-04-03 20:53:17.000000','system','Công ty TNHH Phát hành sách TP.HCM — nhà phân phối sách lớn nhất Việt Nam','cskh@fahasa.com.vn',NULL,'Fahasa','19006467',NULL,NULL),(2,'940 Âu Cơ, Tân Thành, Tân Phú, TP.HCM','2026-04-03 20:53:17.000000','system','Công ty Cổ phần Phát hành sách Phương Nam','info@phuongnambook.com',NULL,'Phương Nam Book','02838650823',NULL,NULL),(3,'56 Đặng Thai Mai, Quốc Tử Giám, Đống Đa, Hà Nội','2026-04-03 20:53:17.000000','system','Công ty CP Sách Alpha — chuyên sách kinh doanh, kỹ năng','info@alphabooks.vn',NULL,'Alpha Books','02473008899',NULL,NULL),(4,'Tòa nhà Viettel, 285 Cách Mạng Tháng Tám, Quận 10, TP.HCM','2026-04-03 20:53:17.000000','system','Công ty TNHH Ti Ki — sàn thương mại điện tử','hotro@tiki.vn',NULL,'Nhà sách Tiki','19006035',NULL,NULL);
/*!40000 ALTER TABLE `suppliers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `address` varchar(255) DEFAULT NULL,
  `avatar` varchar(255) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `email` varchar(255) NOT NULL,
  `full_name` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `refresh_token` mediumtext,
  `updated_at` datetime(6) DEFAULT NULL,
  `updated_by` varchar(255) DEFAULT NULL,
  `role_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKp56c1712k691lhsyewcssf40f` (`role_id`),
  KEY `idx_users_email` (`email`),
  KEY `idx_users_created_at` (`created_at`),
  CONSTRAINT `FKp56c1712k691lhsyewcssf40f` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'TP. Hồ Chí Minh',NULL,'2026-04-03 08:33:16.409453','','admin@bookverse.com','Nguyễn Văn Admin','$2a$10$AYcf66DMf1.j4XZZB90kuuvGB1YrcQLualDN3DnLzB6uiOhBllX8W','0900000001','eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbkBib29rdmVyc2UuY29tIiwiZXhwIjoxNzc3NjA4NDE0LCJpYXQiOjE3NzY3NDQ0MTQsInVzZXIiOnsiaWQiOjEsImVtYWlsIjoiYWRtaW5AYm9va3ZlcnNlLmNvbSIsImZ1bGxOYW1lIjoiTmd1eeG7hW4gVsSDbiBBZG1pbiIsInJvbGUiOm51bGwsImN1c3RvbWVySWQiOm51bGx9fQ.kQeCDnOagJCyIS21KM9ZBg1vPEK_w04lKpo5ADM9tSIb52Vl8ftGmXcT4WZziUk1G-cw3Fjc5H1eCab7M6SA2g','2026-04-21 04:06:54.627504','admin@bookverse.com',1),(2,'Hà Nội',NULL,'2026-04-03 08:33:16.495734','','manager1@bookverse.com','Trần Thị Manager','$2a$10$N.Tsu8oUi8kfqYmNUFbEgOKuClb/LR9vh78vb0Vk6qnsKwxhuDlo.','0900000002',NULL,NULL,NULL,2),(3,'Đà Nẵng',NULL,'2026-04-03 08:33:16.581100','','manager2@bookverse.com','Lê Văn Manager','$2a$10$8BS6fhj1NT8MCCQnFRmif.0h016PnxC8vbRExyj.WIpxMoReMIWqK','0900000003',NULL,NULL,NULL,2),(4,'TP. Hồ Chí Minh',NULL,'2026-04-03 08:33:16.663476','','staff1@bookverse.com','Phạm Thị Nhân Viên','$2a$10$P.mBbqiY6SA7IIFHz6isSealZpOcPWToQCh89dQE6eSx40PQqPIr2','0900000004',NULL,NULL,NULL,3),(5,'Hà Nội',NULL,'2026-04-03 08:33:16.739671','','staff2@bookverse.com','Hoàng Văn Nhân Viên','$2a$10$3fJz.jZifnn51lNgdYihs.pewq82kj6XiEztbCyVsh2jqbmhuZDYC','0900000005',NULL,NULL,NULL,3),(6,'Đà Nẵng',NULL,'2026-04-03 08:33:16.816080','','staff3@bookverse.com','Ngô Thị Nhân Viên','$2a$10$1tVnQcbkPUhLLXbCjy5Jd.o54Wy6MAh.63mpg8iBmzvBzfdZZsGxq','0900000006',NULL,NULL,NULL,3),(7,'Cần Thơ',NULL,'2026-04-03 08:33:16.893329','','staff4@bookverse.com','Đỗ Văn Nhân Viên','$2a$10$Enn17EBtscYRgyqDOJ97huktwXldVrrm0PA9xFyCAycu/4emWrGgS','0900000007',NULL,NULL,NULL,3),(8,'Hải Phòng',NULL,'2026-04-03 08:33:16.968868','','staff5@bookverse.com','Vũ Thị Nhân Viên','$2a$10$gb93Qri/TMnHoqfA828DJOvWifC295/p89Pb9Vjne10AhbKGk.pJ2','0900000008',NULL,NULL,NULL,3),(9,'TP. Hồ Chí Minh',NULL,'2026-04-03 08:33:17.048122','','customer1@bookverse.com','Bùi Văn Khách','$2a$10$bbkNLsF.thG4gsWUJmzwOeNyVGTKZziuucDYB2W3DuuEPIWo7RONm','0900000009','eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJjdXN0b21lcjFAYm9va3ZlcnNlLmNvbSIsImV4cCI6MTc3NzYwODM5NSwiaWF0IjoxNzc2NzQ0Mzk1LCJ1c2VyIjp7ImlkIjo5LCJlbWFpbCI6ImN1c3RvbWVyMUBib29rdmVyc2UuY29tIiwiZnVsbE5hbWUiOiJCw7lpIFbEg24gS2jDoWNoIiwicm9sZSI6bnVsbCwiY3VzdG9tZXJJZCI6MX19.57U9LTMYtKZIkr7Y5gdW1PIm9TCx3zkRNfdm1_BN0qzu-QOwfkRD-7HzXKyFivtkd3FAlt_MAehXBkln7yZ4_Q','2026-04-21 04:06:35.127366','customer1@bookverse.com',4),(10,'Hà Nội',NULL,'2026-04-03 08:33:17.139148','','customer2@bookverse.com','Đặng Thị Khách','$2a$10$4yrXnlwSZHgyRobjivXsFOJZ2amUXoZEApKx8oT4/5SGJdN7V5zDu','0900000010',NULL,'2026-04-08 13:21:18.010709','customer2@bookverse.com',4),(11,'Đà Nẵng',NULL,'2026-04-03 08:33:17.227574','','customer3@bookverse.com','Lý Văn Khách','$2a$10$lhEYEn.5ENgjJFZrXRQ9YuJCMEobH0Z1dkCj9ddk7oZMau.scaOue','0900000011',NULL,NULL,NULL,4),(12,'Cần Thơ',NULL,'2026-04-03 08:33:17.313964','','customer4@bookverse.com','Mai Thị Khách','$2a$10$drUmNxX3WBt1cyjHghvjdOnxs2Alg7BcxqYeIAvUOtkyHXrc0IfZy','0900000012',NULL,NULL,NULL,4),(13,'Hải Phòng',NULL,'2026-04-03 08:33:17.398246','','customer5@bookverse.com','Tô Văn Khách','$2a$10$KLeJsAWEiqdObxoAq4S7MO/Zv6aMsc7Hy/0K0E1MAFtS3iqjpswc2','0900000013',NULL,NULL,NULL,4),(14,'Huế',NULL,'2026-04-03 08:33:17.483618','','customer6@bookverse.com','Phan Thị Khách','$2a$10$3ADc8gTaR5X.i.WURfSUOOfJqkx2IaMbgX4IB1eaCes4AWaegEJEK','0900000014',NULL,NULL,NULL,4),(15,'Nha Trang',NULL,'2026-04-03 08:33:17.568795','','customer7@bookverse.com','Trịnh Văn Khách','$2a$10$Zj2A50zJAgJOd68Xs52Kq.HMBhG1oBNRauvmxjKezeSQSFqaZL1yq','0900000015',NULL,NULL,NULL,4),(16,'Vũng Tàu',NULL,'2026-04-03 08:33:17.653570','','customer8@bookverse.com','Cao Thị Khách','$2a$10$XGJry0I0/NkLO4xkDx.ABukPOKOUCW2tuE0a33uUTKQaqYbP1ixQ2','0900000016',NULL,NULL,NULL,4),(17,'Biên Hòa',NULL,'2026-04-03 08:33:17.749028','','customer9@bookverse.com','Hồ Văn Khách','$2a$10$.QAFnvAhKzx2gJoQyWgQZekz/7cPUDLVGuIplKGvq0R19RfroYePm','0900000017',NULL,NULL,NULL,4),(18,'Thủ Đức',NULL,'2026-04-03 08:33:17.863990','','customer10@bookverse.com','Dương Thị Khách','$2a$10$OQ7HEoLbs89N27q3Ky5wXOfOEeFiG9fzSquwf9MkCjxWSpP0oX.9u','0900000018',NULL,NULL,NULL,4),(19,'Test Address',NULL,'2026-04-08 13:36:29.044406','anonymousUser','test@example.com','Test User','$2a$10$E2XMT8BVyvNTU9moW4BKQ.Z6UWtFgA0RlPS4Q3X2gCzWf5PpGv3fS','0123456789',NULL,'2026-04-08 13:40:48.855797','test@example.com',4);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-04-28 11:08:52
