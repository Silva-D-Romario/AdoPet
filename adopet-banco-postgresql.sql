-- TABELAS

CREATE TABLE public.usuario (
    id character varying(255) NOT NULL PRIMARY KEY,
    atualizado_em timestamp(6) with time zone,
    criado_em timestamp(6) with time zone,
    email character varying(255) NOT NULL,
    bairro character varying(255),
    cep character varying(255),
    cidade character varying(255),
    estado character varying(255),
    numero character varying(255),
    rua character varying(255),
    nome_completo character varying(255) NOT NULL,
    papel character varying(255) NOT NULL,
    senha_hash character varying(255) NOT NULL,
    telefone character varying(255),
    verificado boolean NOT NULL,
    CONSTRAINT usuario_papel_check CHECK (((papel)::text = ANY ((ARRAY['USUARIO'::character varying, 'ADMINISTRADOR'::character varying])::text[])))
);

CREATE TABLE public.animal (
    id character varying(255) NOT NULL PRIMARY KEY,
    atualizado_em timestamp(6) with time zone,
    castrado boolean NOT NULL,
    criado_em timestamp(6) with time zone,
    especie character varying(255) NOT NULL,
    idade integer NOT NULL,
    nome character varying(255) NOT NULL,
    raca character varying(255),
    status character varying(255),
    vacinado boolean NOT NULL,
    doador_id character varying(255),
    CONSTRAINT animal_status_check CHECK (((status)::text = ANY ((ARRAY['DISPONIVEL'::character varying, 'EM_PROCESSO'::character varying, 'ADOTADO'::character varying])::text[])))
);

CREATE TABLE public.solicitacao_adocao (
    id character varying(255) NOT NULL PRIMARY KEY,
    atualizado_em timestamp(6) with time zone,
    criado_em timestamp(6) with time zone,
    status character varying(255),
    animal_id character varying(255),
    solicitante_id character varying(255),
    CONSTRAINT solicitacao_adocao_status_check CHECK (((status)::text = ANY ((ARRAY['PENDENTE'::character varying, 'APROVADA'::character varying, 'RECUSADA'::character varying])::text[])))
);

CREATE TABLE public.adocao (
    id character varying(255) NOT NULL PRIMARY KEY,
    data_adocao timestamp(6) with time zone,
    solicitacao_id character varying(255) UNIQUE
);

CREATE TABLE public.atualizacao_pos_adocao (
    id character varying(255) NOT NULL PRIMARY KEY,
    criado_em timestamp(6) with time zone,
    descricao character varying(255) NOT NULL,
    adocao_id character varying(255) NOT NULL
);

CREATE TABLE public.foto_animal (
    id character varying(255) NOT NULL PRIMARY KEY,
    url text,
    animal_id character varying(255)
);

CREATE TABLE public.foto_atualizacao (
    id character varying(255) NOT NULL PRIMARY KEY,
    url text,
    atualizacao_id character varying(255)
);

CREATE TABLE public.transporte (
    id character varying(255) NOT NULL PRIMARY KEY,
    criado_em timestamp(6) with time zone,
    adocao_id character varying(255) UNIQUE
);

CREATE TABLE public.rastreamento_transporte (
    id character varying(255) NOT NULL PRIMARY KEY,
    criado_em timestamp(6) with time zone,
    mensagem character varying(255),
    status character varying(255),
    transporte_id character varying(255),
    CONSTRAINT rastreamento_transporte_status_check CHECK (((status)::text = ANY ((ARRAY['PENDENTE'::character varying, 'A_CAMINHO'::character varying, 'ENTREGUE'::character varying])::text[])))
);

CREATE TABLE public.denuncia (
    id character varying(255) NOT NULL PRIMARY KEY,
    atualizado_em timestamp(6) with time zone,
    categoria character varying(255) NOT NULL,
    criado_em timestamp(6) with time zone,
    descricao character varying(255) NOT NULL,
    status character varying(255),
    animal_id character varying(255),
    usuario_id character varying(255) NOT NULL,
    usuario_denunciado_id character varying(255),
    CONSTRAINT denuncia_status_check CHECK (((status)::text = ANY ((ARRAY['PENDENTE'::character varying, 'EM_ANALISE'::character varying, 'RESOLVIDA'::character varying, 'DESCARTADA'::character varying])::text[])))
);

CREATE TABLE public.acao_administrativa (
    id character varying(255) NOT NULL PRIMARY KEY,
    criado_em timestamp(6) with time zone,
    descricao character varying(255) NOT NULL,
    tipo character varying(255) NOT NULL,
    administrador_id character varying(255) NOT NULL,
    usuario_alvo_id character varying(255) NOT NULL
);

-- CHAVES ESTRANGEIRAS (RELACIONAMENTOS)

ALTER TABLE ONLY public.animal ADD CONSTRAINT fkd1wnr23169xhi8j5b1u9kq6rm FOREIGN KEY (doador_id) REFERENCES public.usuario(id);
ALTER TABLE ONLY public.solicitacao_adocao ADD CONSTRAINT fkbdj05hve1qh2x8xpaul3jk16c FOREIGN KEY (animal_id) REFERENCES public.animal(id);
ALTER TABLE ONLY public.solicitacao_adocao ADD CONSTRAINT fkfb1pd94r8tu9oymb89lywgv9x FOREIGN KEY (solicitante_id) REFERENCES public.usuario(id);
ALTER TABLE ONLY public.adocao ADD CONSTRAINT fki6t4v84k2rjn1rdo5qldkd93q FOREIGN KEY (solicitacao_id) REFERENCES public.solicitacao_adocao(id);
ALTER TABLE ONLY public.atualizacao_pos_adocao ADD CONSTRAINT fkfluqi0ww8oolh0vnx5una6n58 FOREIGN KEY (adocao_id) REFERENCES public.adocao(id);
ALTER TABLE ONLY public.foto_animal ADD CONSTRAINT fkftkrhgudlbx020pgaqd56htd6 FOREIGN KEY (animal_id) REFERENCES public.animal(id);
ALTER TABLE ONLY public.foto_atualizacao ADD CONSTRAINT fktruyistvr7j5h4xqvso9henba FOREIGN KEY (atualizacao_id) REFERENCES public.atualizacao_pos_adocao(id);
ALTER TABLE ONLY public.transporte ADD CONSTRAINT fkaw6q98agmi15k0p7jijigfy83 FOREIGN KEY (adocao_id) REFERENCES public.adocao(id);
ALTER TABLE ONLY public.rastreamento_transporte ADD CONSTRAINT fkpbmvy0ysayicql5m6ac2ctetu FOREIGN KEY (transporte_id) REFERENCES public.transporte(id);
ALTER TABLE ONLY public.denuncia ADD CONSTRAINT fk9qqwjxefw1uhfgouma4yakj92 FOREIGN KEY (animal_id) REFERENCES public.animal(id);
ALTER TABLE ONLY public.denuncia ADD CONSTRAINT fktjlwue48v7ycj9cu55luadafn FOREIGN KEY (usuario_id) REFERENCES public.usuario(id);
ALTER TABLE ONLY public.denuncia ADD CONSTRAINT fkmq1gk1frw4w5on3fr7u2yjwwm FOREIGN KEY (usuario_denunciado_id) REFERENCES public.usuario(id);
ALTER TABLE ONLY public.acao_administrativa ADD CONSTRAINT fk1vk3qqdhapm6vbjn2a2oc9pl8 FOREIGN KEY (administrador_id) REFERENCES public.usuario(id);
ALTER TABLE ONLY public.acao_administrativa ADD CONSTRAINT fkbpfx0i2mp74rx3mok1bcbgho0 FOREIGN KEY (usuario_alvo_id) REFERENCES public.usuario(id);