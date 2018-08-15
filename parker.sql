-- phpMyAdmin SQL Dump
-- version 4.6.4
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jun 14, 2018 at 06:00 PM
-- Server version: 5.7.14
-- PHP Version: 5.6.25

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `parker`
--

-- --------------------------------------------------------

--
-- Table structure for table `driver`
--

CREATE TABLE `driver` (
  `driver_id` int(11) NOT NULL,
  `name` varchar(100) NOT NULL,
  `phone_number` varchar(20) NOT NULL,
  `car_number_plate` varchar(20) NOT NULL,
  `email` varchar(100) NOT NULL,
  `password` varchar(20) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `driver`
--

INSERT INTO `driver` (`driver_id`, `name`, `phone_number`, `car_number_plate`, `email`, `password`) VALUES
(1, '', '', '', '', ''),
(2, 'f', 'f', 'f', '', 'f'),
(3, 'f', 'f', 'f', 'f', 'f'),
(4, 'jusi', '0750', 'UBB 123B', 'teberyowajustine123@gmail.com', 'c'),
(5, 'Micheal', '07000', 'UBB 111Z', 'm@gmail.com', 'm');

-- --------------------------------------------------------

--
-- Table structure for table `parking_lot`
--

CREATE TABLE `parking_lot` (
  `parking_lot_id` int(10) NOT NULL,
  `name` varchar(50) NOT NULL,
  `address` varchar(50) NOT NULL,
  `description` varchar(100) NOT NULL DEFAULT 'description',
  `gps_codes` varchar(100) NOT NULL,
  `number_of_slots` varchar(10) NOT NULL,
  `email` varchar(50) NOT NULL,
  `phone_number` varchar(50) NOT NULL,
  `password` varchar(50) NOT NULL,
  `photo_external` varchar(50) NOT NULL,
  `photo_internal` varchar(50) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `parking_lot`
--

INSERT INTO `parking_lot` (`parking_lot_id`, `name`, `address`, `description`, `gps_codes`, `number_of_slots`, `email`, `phone_number`, `password`, `photo_external`, `photo_internal`) VALUES
(1, 'Small Parking Lot', 'Kawempe', 'description', '0.5, 0.8', '30', 'small@gmail.com', '0704873305', '123', 'x1.png', 'x2.png'),
(5, 'reagan', '', 'description', '0.3328284156777764,32.56936904042959', '25', 'email', '0705', 'yy', '0705_Exterior.jpg', '0705_Interor.jpg'),
(6, 'izo Park', '', 'description', '0.32835992984444035,32.57099445909262', '23', 'isaaconline96@gmail.com', '0708', 'f', '0708_Exterior.jpg', '0708_Interor.jpg'),
(7, 'izo Park', '', 'description', '0.32835992984444035,32.57099445909262', '23', 'isaaconline96@gmail.com', '0708', 'f', '0708_Exterior.jpg', '0708_Interor.jpg'),
(8, 'Mulago A', 'Kawempe', 'description', '0.3373756880109159,32.57825452834368', '50', 'im@gmail.com', '0704873305', 'im', '0704873305_Exterior.jpg', '0704873305_Interor.jpg');

-- --------------------------------------------------------

--
-- Table structure for table `parking_record`
--

CREATE TABLE `parking_record` (
  `parking_lot_id` varchar(10) NOT NULL,
  `parking_record_id` int(11) NOT NULL,
  `name` varchar(50) NOT NULL,
  `plate_number` varchar(50) NOT NULL,
  `today` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gps_codes` varchar(50) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `parking_record`
--

INSERT INTO `parking_record` (`parking_lot_id`, `parking_record_id`, `name`, `plate_number`, `today`, `gps_codes`) VALUES
('', 3, 's', '', '2018-06-14 07:03:33', ''),
('5', 2, 'f', 'f', '2018-06-14 06:43:28', 'gps'),
('6', 5, 'jusi', 'UBB 123B', '2018-06-14 07:56:45', 'gps'),
('8', 6, 'Micheal', 'UBB 111Z', '2018-06-14 08:02:44', 'gps');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `driver`
--
ALTER TABLE `driver`
  ADD UNIQUE KEY `driver_id` (`driver_id`);

--
-- Indexes for table `parking_lot`
--
ALTER TABLE `parking_lot`
  ADD PRIMARY KEY (`parking_lot_id`);

--
-- Indexes for table `parking_record`
--
ALTER TABLE `parking_record`
  ADD PRIMARY KEY (`parking_record_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `driver`
--
ALTER TABLE `driver`
  MODIFY `driver_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;
--
-- AUTO_INCREMENT for table `parking_lot`
--
ALTER TABLE `parking_lot`
  MODIFY `parking_lot_id` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;
--
-- AUTO_INCREMENT for table `parking_record`
--
ALTER TABLE `parking_record`
  MODIFY `parking_record_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
