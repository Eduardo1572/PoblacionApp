% ======================================================================================
%  BASE DE CONOCIMIENTOS: App de Consulta de Población   
%  Descripción: Se decidio dividir los hechos geográficos y demográficos en 4 niveles jerárquicos.
%
%  UNIDADES DE MEDIDA PARA CADA NIVEL:
%    - Continentes y Países : representado en millones de habitantes
%    - Estados y Ciudades   : representado en miles de habitantes
% ======================================================================================


% -------------------------------------------------------------
%  NIVEL 1: CONTINENTES
%  Estructura del Predicado: continente(Nombre, PoblacionMillones)
% -------------------------------------------------------------

continente(america, 1050).
continente(europa,   744).
continente(asia,    4863).


% ------------------------------------------------------------
%  NIVEL 2: PAÍSES
%  Estructura del Predicado: pais(Nombre, Continente, PoblacionMillones)
% ------------------------------------------------------------

% América
pais(mexico,         america, 133).
pais(brasil,         america, 218).
pais(estados_unidos, america, 345).

% Europa
pais(alemania, europa, 84).
pais(francia,  europa, 68).
pais(espana,   europa, 48).

% Asia
pais(china, asia, 1410).
pais(india, asia, 1450).
pais(japon, asia,  123).


% ------------------------------------------------------------
%  NIVEL 3: ESTADOS / PROVINCIAS
%  Estructura del Predicado: estado(Nombre, Pais, PoblacionMiles)
% ------------------------------------------------------------

% Estados de México
estado(ciudad_de_mexico, mexico, 9209).
estado(jalisco,          mexico, 8500).
estado(nuevo_leon,       mexico, 6000).
estado(puebla,           mexico, 6700).
estado(veracruz,         mexico, 8100).

% Estados de Brasil 
estado(sao_paulo_estado,      brasil, 44411).
estado(rio_de_janeiro_estado, brasil, 16054).
estado(minas_gerais,          brasil, 20538).
estado(bahia,                 brasil, 14141).
estado(parana,                brasil, 11444).

% Estados de Alemania
estado(renania,        alemania, 18139).
estado(baviera,        alemania, 13400).
estado(baden,          alemania, 11300).
estado(berlin_estado,  alemania,  3878).
estado(hamburgo_estado,alemania,  1900).


% ------------------------------------------------------------
%  NIVEL 4: CIUDADES
%  Estructura del Predicado: ciudad(Nombre, Estado, PoblacionMiles)
% ------------------------------------------------------------

ciudad(guadalajara, jalisco, 1385).
ciudad(zapopan,     jalisco, 1476).
ciudad(tlaquepaque, jalisco,  687).


ciudad(sao_paulo_ciudad, sao_paulo_estado, 11451).
ciudad(campinas,         sao_paulo_estado,  1138).
ciudad(guarulhos,        sao_paulo_estado,  1291).


ciudad(munich,     baviera, 1512).
ciudad(nuremberg,  baviera,  523).
ciudad(augsburgo,  baviera,  301).