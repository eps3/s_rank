-- phpMyAdmin SQL Dump
-- version 4.4.15.1
-- http://www.phpmyadmin.net
--
-- Host: 10.1.32.43
-- Generation Time: 2017-10-22 03:56:13
-- 服务器版本： 5.6.37
-- PHP Version: 5.3.8

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `test`
--

-- --------------------------------------------------------

--
-- 表的结构 `t_base`
--

CREATE TABLE IF NOT EXISTS `t_base` (
  `c_id` int(11) NOT NULL,
  `c_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `c_create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- 表的结构 `t_card`
--

CREATE TABLE IF NOT EXISTS `t_card` (
  `c_id` int(11) NOT NULL,
  `c_topic_id` int(11) NOT NULL,
  `c_user_id` int(11) NOT NULL,
  `c_content` varchar(256) COLLATE utf8mb4_unicode_ci NOT NULL,
  `c_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `c_create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- 表的结构 `t_follow`
--

CREATE TABLE IF NOT EXISTS `t_follow` (
  `c_id` int(11) NOT NULL,
  `c_topic_id` int(11) NOT NULL,
  `c_user_id` int(11) NOT NULL,
  `c_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `c_create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- 表的结构 `t_topic`
--

CREATE TABLE IF NOT EXISTS `t_topic` (
  `c_id` int(11) NOT NULL,
  `c_name` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL,
  `c_desc` varchar(256) COLLATE utf8mb4_unicode_ci NOT NULL,
  `c_user_id` int(11) NOT NULL,
  `c_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `c_create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- 表的结构 `t_user`
--

CREATE TABLE IF NOT EXISTS `t_user` (
  `c_id` int(11) NOT NULL,
  `c_name` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL,
  `c_email` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL,
  `c_desc` varchar(256) COLLATE utf8mb4_unicode_ci NOT NULL,
  `c_img_url` varchar(128) COLLATE utf8mb4_unicode_ci NOT NULL,
  `c_role` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL,
  `c_password` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL,
  `c_salt` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL,
  `c_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `c_create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `t_base`
--
ALTER TABLE `t_base`
  ADD PRIMARY KEY (`c_id`);

--
-- Indexes for table `t_card`
--
ALTER TABLE `t_card`
  ADD PRIMARY KEY (`c_id`);

--
-- Indexes for table `t_follow`
--
ALTER TABLE `t_follow`
  ADD PRIMARY KEY (`c_id`),
  ADD UNIQUE KEY `c_user_id` (`c_user_id`,`c_topic_id`) USING BTREE;

--
-- Indexes for table `t_topic`
--
ALTER TABLE `t_topic`
  ADD PRIMARY KEY (`c_id`),
  ADD UNIQUE KEY `c_name` (`c_name`);

--
-- Indexes for table `t_user`
--
ALTER TABLE `t_user`
  ADD PRIMARY KEY (`c_id`),
  ADD UNIQUE KEY `c_name` (`c_name`),
  ADD UNIQUE KEY `c_email` (`c_email`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `t_base`
--
ALTER TABLE `t_base`
  MODIFY `c_id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `t_card`
--
ALTER TABLE `t_card`
  MODIFY `c_id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `t_follow`
--
ALTER TABLE `t_follow`
  MODIFY `c_id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `t_topic`
--
ALTER TABLE `t_topic`
  MODIFY `c_id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `t_user`
--
ALTER TABLE `t_user`
  MODIFY `c_id` int(11) NOT NULL AUTO_INCREMENT;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
