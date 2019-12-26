CREATE TABLE cargo (codCar INTEGER NOT NULL, 
                    descCar VARCHAR(50),
					CONSTRAINT codCar_pkey PRIMARY KEY (codCar))
CREATE TABLE orgao (codOrg INTEGER NOT NULL, 
                    descOrg VARCHAR(50)
					CONSTRAINT codOrg_pkey PRIMARY KEY (codOrg))
CREATE TABLE sistema(codSis INTEGER NOT NULL, , 
                     descSis VARCHAR(50)
					 CONSTRAINT codSis_pkey PRIMARY KEY (codSis))

CREATE TABLE usuario
(
  id bigserial NOT NULL,
  codCar NOT NULL, 
  codOrg NOT NULL,
  cpf    NOT NULL,
  dataalteracao timestamp without time zone,
  datacadastro timestamp without time zone,
  email character varying(64) NOT NULL,
  foto character varying(255),
  nivel character varying(64) NOT NULL,
  senha character varying(32) NOT NULL,
  status character varying(1) NOT NULL,
  usuario character varying(32) NOT NULL,
  usuarioalteracao bigint,
  CONSTRAINT usuario_pkey PRIMARY KEY (id),
  CONSTRAINT uk_email UNIQUE (email),
  CONSTRAINT uk_CPF UNIQUE (cpf),
  CONSTRAINT uk_usuario UNIQUE (usuario)
  
)

CREATE TABLE usuariosSistem(codUsu NOT NULL, 
                            codSis NOT NULL
							CONSTRAINT codUsuSis_pkey PRIMARY KEY (codSis, codUsu)
							FOREIGN KEY (codSis) REFERENCES sistema (codSis),
							FOREIGN KEY (codUsu) REFERENCES usuario (id))
                                             