% ============================================================
%  REGLAS DE INFERENCIA: App de Consulta de Población.
%  Descripcion: Aqui estan las reglas que seguira prolog 
% ============================================================


% ------------------------------------------------------------
%  REGLAS DE JERARQUÍA GEOGRÁFICA
% ------------------------------------------------------------

% Ciudad está en Pais SI la Ciudad pertenece a un Estado Y (,) ese Estado pertenece al Pais.
ciudad_en_pais(Ciudad, Pais) :- % :- es el equivalente al if IF
    ciudad(Ciudad, Estado, _),
    estado(Estado, Pais, _).

ciudad_en_continente(Ciudad, Continente) :-
    ciudad_en_pais(Ciudad, Pais),
    pais(Pais, Continente, _).

estado_en_continente(Estado, Continente) :-
    estado(Estado, Pais, _),
    pais(Pais, Continente, _).


% ------------------------------------------------------------
%  REGLAS DE RANKING — PAÍSES POR CONTINENTE
%  CORRECCIÓN: OtroPais reemplazado por _ (no lo usamos)
% ------------------------------------------------------------


% Un país es el más poblado SI tiene cierta Poblacion Y NO (\+) existe otro país en ese continente con una 
% población mayor (OtraPob > Poblacion).
pais_mas_poblado(Continente, Pais, Poblacion) :-
    pais(Pais, Continente, Poblacion),
    \+ (pais(_, Continente, OtraPob), % \+ (negacion por fallo) se usa para comprobar que algo NO se puede demostrar.
        OtraPob > Poblacion).

pais_menos_poblado(Continente, Pais, Poblacion) :-
    pais(Pais, Continente, Poblacion),
    \+ (pais(_, Continente, OtraPob),
        OtraPob < Poblacion).


% ------------------------------------------------------------
%  REGLAS DE RANKING — ESTADOS POR PAÍS
%  CORRECCIÓN: OtroEstado reemplazado por _
% ------------------------------------------------------------

estado_mas_poblado(Pais, Estado, Poblacion) :-
    estado(Estado, Pais, Poblacion),
    \+ (estado(_, Pais, OtraPob),
        OtraPob > Poblacion).

estado_menos_poblado(Pais, Estado, Poblacion) :-
    estado(Estado, Pais, Poblacion),
    \+ (estado(_, Pais, OtraPob),
        OtraPob < Poblacion).


% ------------------------------------------------------------
%  REGLAS DE RANKING — CIUDADES POR ESTADO
%  CORRECCIÓN: OtraCiudad reemplazado por _
% ------------------------------------------------------------

ciudad_mas_poblada(Estado, Ciudad, Poblacion) :-
    ciudad(Ciudad, Estado, Poblacion),
    \+ (ciudad(_, Estado, OtraPob),
        OtraPob > Poblacion).


% ------------------------------------------------------------
%  REGLA DE RANKING — CONTINENTE MÁS POBLADO
% ------------------------------------------------------------

continente_mas_poblado(Continente, Poblacion) :-
    continente(Continente, Poblacion),
    \+ (continente(_, OtraPob),
        OtraPob > Poblacion).

% ------------------------------------------------------------
%  REGLAS DE BÚSQUEDA FLEXIBLE
% ------------------------------------------------------------

% Transforma el texto ingresado a minusculas para que la búsqueda coincida exactamente con los datos de la base 
% de conocimientos (que están en minúsculas), evitando errores si el usuario usa mayúsculas.
buscar_pais(EntradaUsuario, Pais, Continente, Poblacion) :-
    downcase_atom(EntradaUsuario, EntradaMin),
    pais(Pais, Continente, Poblacion),
    downcase_atom(Pais, PaisMin), %downcas_atom lo que hace es tomar un átomo (texto) y lo convierte a minúsculas. 
    EntradaMin = PaisMin.

buscar_estado(EntradaUsuario, Estado, Pais, Poblacion) :-
    downcase_atom(EntradaUsuario, EntradaMin),
    estado(Estado, Pais, Poblacion),
    downcase_atom(Estado, EstadoMin),
    EntradaMin = EstadoMin.

buscar_ciudad(EntradaUsuario, Ciudad, Estado, Poblacion) :-
    downcase_atom(EntradaUsuario, EntradaMin),
    ciudad(Ciudad, Estado, Poblacion),
    downcase_atom(Ciudad, CiudadMin),
    EntradaMin = CiudadMin.