-- phpMyAdmin SQL Dump
-- version 4.1.14
-- http://www.phpmyadmin.net
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 06-07-2017 a las 16:26:55
-- Versión del servidor: 5.6.17
-- Versión de PHP: 5.5.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Base de datos: `reservapp`
--
CREATE DATABASE IF NOT EXISTS `reservapp` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
USE `reservapp`;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `establecimiento`
--

DROP TABLE IF EXISTS `establecimiento`;
CREATE TABLE IF NOT EXISTS `establecimiento` (
  `codigoEstablecimiento` int(11) unsigned NOT NULL,
  `Nombre` varchar(45) CHARACTER SET utf8 DEFAULT NULL,
  `Nit` varchar(9) CHARACTER SET utf8 NOT NULL,
  `Direccion` varchar(45) CHARACTER SET utf8 DEFAULT NULL,
  `Correo` varchar(45) CHARACTER SET utf8 DEFAULT NULL,
  `Telefono` varchar(45) CHARACTER SET utf8 DEFAULT NULL,
  `Longitud` varchar(45) CHARACTER SET utf8 DEFAULT NULL,
  `Latitud` varchar(45) CHARACTER SET utf8 DEFAULT NULL,
  `Mesas` int(11) NOT NULL,
  `documento` varchar(65) COLLATE utf8_spanish_ci DEFAULT NULL,
  PRIMARY KEY (`codigoEstablecimiento`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;

--
-- Volcado de datos para la tabla `establecimiento`
--

INSERT INTO `establecimiento` (`codigoEstablecimiento`, `Nombre`, `Nit`, `Direccion`, `Correo`, `Telefono`, `Longitud`, `Latitud`, `Mesas`, `documento`) VALUES
(25, 'ventas', '7', '8', '8', '8', '8', '8', 10, NULL);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `factura`
--

DROP TABLE IF EXISTS `factura`;
CREATE TABLE IF NOT EXISTS `factura` (
  `CodigoFactura` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `MetodoPago` varchar(45) CHARACTER SET utf8 DEFAULT NULL,
  `ValorTotal` float DEFAULT NULL,
  `EstadoPago` tinyint(1) DEFAULT NULL,
  `CodigoReserva` int(11) unsigned NOT NULL,
  PRIMARY KEY (`CodigoFactura`),
  KEY `fk_factura_reserva1_idx` (`CodigoReserva`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci AUTO_INCREMENT=4 ;

--
-- Volcado de datos para la tabla `factura`
--

INSERT INTO `factura` (`CodigoFactura`, `MetodoPago`, `ValorTotal`, `EstadoPago`, `CodigoReserva`) VALUES
(3, '3', 3, 3, 3);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `menu`
--

DROP TABLE IF EXISTS `menu`;
CREATE TABLE IF NOT EXISTS `menu` (
  `CodigoMenu` int(11) NOT NULL,
  `Nombre` varchar(45) CHARACTER SET utf8 NOT NULL,
  `Precio` int(11) NOT NULL,
  `Descripcion` varchar(45) CHARACTER SET utf8 DEFAULT NULL,
  `Imagen` varchar(45) CHARACTER SET utf8 DEFAULT NULL,
  `CodigoEstablecimiento` int(11) unsigned NOT NULL,
  `estado` int(1) DEFAULT NULL,
  PRIMARY KEY (`CodigoMenu`),
  KEY `fk_menu_establecimiento1_idx` (`CodigoEstablecimiento`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;

--
-- Volcado de datos para la tabla `menu`
--

INSERT INTO `menu` (`CodigoMenu`, `Nombre`, `Precio`, `Descripcion`, `Imagen`, `CodigoEstablecimiento`, `estado`) VALUES
(3, '3', 3, '3', '3', 25, 3);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `mesa`
--

DROP TABLE IF EXISTS `mesa`;
CREATE TABLE IF NOT EXISTS `mesa` (
  `CodigoMesa` int(11) unsigned NOT NULL,
  `Puestos` int(11) NOT NULL,
  `Ubicacion` varchar(45) CHARACTER SET utf8 DEFAULT NULL,
  `Estado` tinyint(1) DEFAULT NULL,
  `codigoEstablecimiento` int(11) unsigned NOT NULL,
  PRIMARY KEY (`CodigoMesa`),
  KEY `fk_mesa_establecimiento1_idx` (`codigoEstablecimiento`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;

--
-- Volcado de datos para la tabla `mesa`
--

INSERT INTO `mesa` (`CodigoMesa`, `Puestos`, `Ubicacion`, `Estado`, `codigoEstablecimiento`) VALUES
(3, 3, '3', 0, 25);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `orden`
--

DROP TABLE IF EXISTS `orden`;
CREATE TABLE IF NOT EXISTS `orden` (
  `CodigoOrden` int(11) NOT NULL,
  `Nombre` varchar(45) CHARACTER SET utf8 NOT NULL,
  `Precio` int(11) NOT NULL,
  `Descripcion` varchar(45) CHARACTER SET utf8 DEFAULT NULL,
  `Imagen` varchar(45) CHARACTER SET utf8 DEFAULT NULL,
  `estado` int(1) DEFAULT NULL,
  `menu_CodigoMenu` int(11) NOT NULL,
  PRIMARY KEY (`CodigoOrden`),
  KEY `fk_orden_menu1_idx` (`menu_CodigoMenu`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;

--
-- Volcado de datos para la tabla `orden`
--

INSERT INTO `orden` (`CodigoOrden`, `Nombre`, `Precio`, `Descripcion`, `Imagen`, `estado`, `menu_CodigoMenu`) VALUES
(3, '3', 3, '3', '3', 3, 3);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `reserva`
--

DROP TABLE IF EXISTS `reserva`;
CREATE TABLE IF NOT EXISTS `reserva` (
  `CodigoReserva` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `Fecha Reserva` date DEFAULT NULL,
  `CodigoMesa` int(11) unsigned DEFAULT NULL,
  `orden_CodigoOrden` int(11) DEFAULT NULL,
  `comentario` varchar(300) COLLATE utf8_spanish_ci DEFAULT NULL,
  `usuario_documento` varchar(15) CHARACTER SET utf8 DEFAULT NULL,
  PRIMARY KEY (`CodigoReserva`),
  KEY `fk_reserva_mesa1_idx` (`CodigoMesa`),
  KEY `fk_reserva_orden1_idx` (`orden_CodigoOrden`),
  KEY `fk_reserva_usuario1_idx` (`usuario_documento`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci AUTO_INCREMENT=5 ;

--
-- Volcado de datos para la tabla `reserva`
--

INSERT INTO `reserva` (`CodigoReserva`, `Fecha Reserva`, `CodigoMesa`, `orden_CodigoOrden`, `comentario`, `usuario_documento`) VALUES
(3, '0000-00-00', 3, 3, '3', '23145'),
(4, '0000-00-00', 3, 3, '3', '23145');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `rol`
--

DROP TABLE IF EXISTS `rol`;
CREATE TABLE IF NOT EXISTS `rol` (
  `codigodelRol` int(11) unsigned NOT NULL,
  `Nombre` varchar(45) CHARACTER SET utf8 NOT NULL,
  PRIMARY KEY (`codigodelRol`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;

--
-- Volcado de datos para la tabla `rol`
--

INSERT INTO `rol` (`codigodelRol`, `Nombre`) VALUES
(1, 'Administrador'),
(2, 'Gerente'),
(3, 'Usuario final');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuario`
--

DROP TABLE IF EXISTS `usuario`;
CREATE TABLE IF NOT EXISTS `usuario` (
  `documento` varchar(15) COLLATE utf8_spanish_ci NOT NULL,
  `Correo` varchar(45) CHARACTER SET utf8 DEFAULT NULL,
  `Contrasena` varchar(45) CHARACTER SET utf8 DEFAULT NULL,
  `Nombre` varchar(50) CHARACTER SET utf8 NOT NULL,
  `Apellido` varchar(45) CHARACTER SET utf8 NOT NULL,
  `Telefono` varchar(45) CHARACTER SET utf8 DEFAULT NULL,
  `Direccion` varchar(45) CHARACTER SET utf8 DEFAULT NULL,
  `Sexo` varchar(10) CHARACTER SET utf8 DEFAULT NULL,
  `rol_codigodelRol` int(11) unsigned DEFAULT NULL,
  PRIMARY KEY (`documento`),
  KEY `fk_usuario_rol1_idx` (`rol_codigodelRol`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;

--
-- Volcado de datos para la tabla `usuario`
--

INSERT INTO `usuario` (`documento`, `Correo`, `Contrasena`, `Nombre`, `Apellido`, `Telefono`, `Direccion`, `Sexo`, `rol_codigodelRol`) VALUES
('123456', 'admin@admin.com', '1234567', 'Leidy', 'Gamba', '123456', 'Cll 6 kr 5No. 10', 'Femenino', 1),
('23145', 'segura@segura.com', '12345', 'rochi', 'Segura', '123456', 'cll 4 krr 3 a', 'Masculino', 1);

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `factura`
--
ALTER TABLE `factura`
  ADD CONSTRAINT `fk_factura_reserva1` FOREIGN KEY (`CodigoReserva`) REFERENCES `reserva` (`CodigoReserva`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Filtros para la tabla `menu`
--
ALTER TABLE `menu`
  ADD CONSTRAINT `fk_menu_establecimiento1` FOREIGN KEY (`CodigoEstablecimiento`) REFERENCES `establecimiento` (`codigoEstablecimiento`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Filtros para la tabla `mesa`
--
ALTER TABLE `mesa`
  ADD CONSTRAINT `fk_mesa_establecimiento1` FOREIGN KEY (`codigoEstablecimiento`) REFERENCES `establecimiento` (`codigoEstablecimiento`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Filtros para la tabla `orden`
--
ALTER TABLE `orden`
  ADD CONSTRAINT `fk_orden_menu1` FOREIGN KEY (`menu_CodigoMenu`) REFERENCES `menu` (`CodigoMenu`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Filtros para la tabla `reserva`
--
ALTER TABLE `reserva`
  ADD CONSTRAINT `fk_reserva_mesa1` FOREIGN KEY (`CodigoMesa`) REFERENCES `mesa` (`CodigoMesa`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_reserva_orden1` FOREIGN KEY (`orden_CodigoOrden`) REFERENCES `orden` (`CodigoOrden`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Filtros para la tabla `usuario`
--
ALTER TABLE `usuario`
  ADD CONSTRAINT `fk_usuario_rol1` FOREIGN KEY (`rol_codigodelRol`) REFERENCES `rol` (`codigodelRol`) ON DELETE NO ACTION ON UPDATE NO ACTION;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
