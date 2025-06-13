--
-- PostgreSQL database dump
--

-- Dumped from database version 14.17 (Homebrew)
-- Dumped by pg_dump version 14.17 (Homebrew)

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: administre; Type: TABLE; Schema: public; Owner: julienlarzul
--

CREATE TABLE public.administre (
    nomutilisateur character varying(50) NOT NULL,
    nomespace character varying(50) NOT NULL
);


ALTER TABLE public.administre OWNER TO julienlarzul;

--
-- Name: appartient; Type: TABLE; Schema: public; Owner: julienlarzul
--

CREATE TABLE public.appartient (
    nomutilisateur character varying(50) NOT NULL,
    nomespace character varying(50) NOT NULL
);


ALTER TABLE public.appartient OWNER TO julienlarzul;

--
-- Name: canal; Type: TABLE; Schema: public; Owner: julienlarzul
--

CREATE TABLE public.canal (
    nomcanal character varying(50) NOT NULL,
    typecanal character varying(50),
    nomespace character varying(50) NOT NULL
);


ALTER TABLE public.canal OWNER TO julienlarzul;

--
-- Name: espacetravail; Type: TABLE; Schema: public; Owner: julienlarzul
--

CREATE TABLE public.espacetravail (
    nomespace character varying(50) NOT NULL,
    description character varying(50)
);


ALTER TABLE public.espacetravail OWNER TO julienlarzul;

--
-- Name: invitationcanal; Type: TABLE; Schema: public; Owner: julienlarzul
--

CREATE TABLE public.invitationcanal (
    nomutilisateur character varying(50) NOT NULL,
    nomespace character varying(50) NOT NULL,
    emailinvitation character varying(50),
    statut character varying(50)
);


ALTER TABLE public.invitationcanal OWNER TO julienlarzul;

--
-- Name: invitationespace; Type: TABLE; Schema: public; Owner: julienlarzul
--

CREATE TABLE public.invitationespace (
    nomutilisateur character varying(50) NOT NULL,
    nomespace character varying(50) NOT NULL,
    emailinvitation character varying(50),
    statut character varying(50)
);


ALTER TABLE public.invitationespace OWNER TO julienlarzul;

--
-- Name: message; Type: TABLE; Schema: public; Owner: julienlarzul
--

CREATE TABLE public.message (
    idmessage integer NOT NULL,
    contenu character varying(50),
    nomutilisateur character varying(50) NOT NULL,
    nomutilisateur_1 character varying(50) NOT NULL,
    nomutilisateur_2 character varying(50) NOT NULL,
    nomcanal character varying(50),
    time_ timestamp without time zone
);


ALTER TABLE public.message OWNER TO julienlarzul;

--
-- Name: reaction; Type: TABLE; Schema: public; Owner: julienlarzul
--

CREATE TABLE public.reaction (
    nomutilisateur character varying(50) NOT NULL,
    idmessage integer NOT NULL,
    typereaction character varying(50)
);


ALTER TABLE public.reaction OWNER TO julienlarzul;

--
-- Name: utilisateur; Type: TABLE; Schema: public; Owner: julienlarzul
--

CREATE TABLE public.utilisateur (
    nomutilisateur character varying(50) NOT NULL,
    email character varying(50),
    motdepasse character varying(50)
);


ALTER TABLE public.utilisateur OWNER TO julienlarzul;

--
-- Data for Name: administre; Type: TABLE DATA; Schema: public; Owner: julienlarzul
--

COPY public.administre (nomutilisateur, nomespace) FROM stdin;
alice	DevTeam
bob	RH
\.


--
-- Data for Name: appartient; Type: TABLE DATA; Schema: public; Owner: julienlarzul
--

COPY public.appartient (nomutilisateur, nomespace) FROM stdin;
alice	DevTeam
bob	DevTeam
bob	RH
carol	RH
carol	DevTeam
\.


--
-- Data for Name: canal; Type: TABLE DATA; Schema: public; Owner: julienlarzul
--

COPY public.canal (nomcanal, typecanal, nomespace) FROM stdin;
general	public	DevTeam
recrutement	privé	RH
\.


--
-- Data for Name: espacetravail; Type: TABLE DATA; Schema: public; Owner: julienlarzul
--

COPY public.espacetravail (nomespace, description) FROM stdin;
DevTeam	Espace de développement
RH	Espace des ressources humaines
\.


--
-- Data for Name: invitationcanal; Type: TABLE DATA; Schema: public; Owner: julienlarzul
--

COPY public.invitationcanal (nomutilisateur, nomespace, emailinvitation, statut) FROM stdin;
alice	DevTeam	zack@mail.com	refusée
carol	RH	lucie@mail.com	acceptée
\.


--
-- Data for Name: invitationespace; Type: TABLE DATA; Schema: public; Owner: julienlarzul
--

COPY public.invitationespace (nomutilisateur, nomespace, emailinvitation, statut) FROM stdin;
alice	DevTeam	david@mail.com	en attente
carol	RH	emma@mail.com	acceptée
\.


--
-- Data for Name: message; Type: TABLE DATA; Schema: public; Owner: julienlarzul
--

COPY public.message (idmessage, contenu, nomutilisateur, nomutilisateur_1, nomutilisateur_2, nomcanal, time_) FROM stdin;
1	Bienvenue Bob	alice	alice	bob	general	2025-01-01 08:30:00
2	Merci !	bob	bob	alice	general	2025-01-01 08:31:00
3	Recrutement ouvert ?	carol	carol	bob	recrutement	2025-01-01 10:00:00
4	Salut !	alice	alice	bob	\N	2025-01-01 14:00:00
5	Bonjour	alice	alice	bob	\N	2025-01-01 18:05:16
6	j'ai dit deux fois bonjour c'est marrant	alice	alice	bob	\N	2025-01-01 18:05:38
7	test depuis form	alice	alice	bob	\N	2025-01-01 18:05:56
8	Ça dit quoi l'équipe ?	alice	alice	alice	general	2025-01-01 18:08:27
9	super les frères parlez un peu la	alice	alice	bob	\N	2025-01-01 18:10:27
10	Coucou	carol	carol	carol	general	2025-01-01 18:28:59
11	Ça va super	bob	bob	bob	general	2025-01-01 18:29:18
12	youpi	alice	alice	bob	\N	2025-01-01 18:34:44
13	test	alice	alice	bob	\N	2025-06-13 10:15:07.460094
14	ok	alice	alice	bob	\N	2025-06-13 10:26:52.430283
15	test	alice	alice	alice	general	2025-06-13 10:28:15.891323
16	salut	alice	alice	carol	\N	2025-06-13 10:36:33.721217
\.


--
-- Data for Name: reaction; Type: TABLE DATA; Schema: public; Owner: julienlarzul
--

COPY public.reaction (nomutilisateur, idmessage, typereaction) FROM stdin;
bob	1	like
carol	2	love
alice	1	funny
alice	4	love
alice	5	funny
\.


--
-- Data for Name: utilisateur; Type: TABLE DATA; Schema: public; Owner: julienlarzul
--

COPY public.utilisateur (nomutilisateur, email, motdepasse) FROM stdin;
alice	alice@mail.com	mdp123
bob	bob@mail.com	bobpass
carol	carol@mail.com	carolpw
\.


--
-- Name: administre administre_pkey; Type: CONSTRAINT; Schema: public; Owner: julienlarzul
--

ALTER TABLE ONLY public.administre
    ADD CONSTRAINT administre_pkey PRIMARY KEY (nomutilisateur, nomespace);


--
-- Name: appartient appartient_pkey; Type: CONSTRAINT; Schema: public; Owner: julienlarzul
--

ALTER TABLE ONLY public.appartient
    ADD CONSTRAINT appartient_pkey PRIMARY KEY (nomutilisateur, nomespace);


--
-- Name: canal canal_pkey; Type: CONSTRAINT; Schema: public; Owner: julienlarzul
--

ALTER TABLE ONLY public.canal
    ADD CONSTRAINT canal_pkey PRIMARY KEY (nomcanal);


--
-- Name: espacetravail espacetravail_pkey; Type: CONSTRAINT; Schema: public; Owner: julienlarzul
--

ALTER TABLE ONLY public.espacetravail
    ADD CONSTRAINT espacetravail_pkey PRIMARY KEY (nomespace);


--
-- Name: invitationcanal invitationcanal_pkey; Type: CONSTRAINT; Schema: public; Owner: julienlarzul
--

ALTER TABLE ONLY public.invitationcanal
    ADD CONSTRAINT invitationcanal_pkey PRIMARY KEY (nomutilisateur, nomespace);


--
-- Name: invitationespace invitationespace_pkey; Type: CONSTRAINT; Schema: public; Owner: julienlarzul
--

ALTER TABLE ONLY public.invitationespace
    ADD CONSTRAINT invitationespace_pkey PRIMARY KEY (nomutilisateur, nomespace);


--
-- Name: message message_pkey; Type: CONSTRAINT; Schema: public; Owner: julienlarzul
--

ALTER TABLE ONLY public.message
    ADD CONSTRAINT message_pkey PRIMARY KEY (idmessage);


--
-- Name: reaction reaction_pkey; Type: CONSTRAINT; Schema: public; Owner: julienlarzul
--

ALTER TABLE ONLY public.reaction
    ADD CONSTRAINT reaction_pkey PRIMARY KEY (nomutilisateur, idmessage);


--
-- Name: utilisateur utilisateur_pkey; Type: CONSTRAINT; Schema: public; Owner: julienlarzul
--

ALTER TABLE ONLY public.utilisateur
    ADD CONSTRAINT utilisateur_pkey PRIMARY KEY (nomutilisateur);


--
-- Name: administre administre_nomespace_fkey; Type: FK CONSTRAINT; Schema: public; Owner: julienlarzul
--

ALTER TABLE ONLY public.administre
    ADD CONSTRAINT administre_nomespace_fkey FOREIGN KEY (nomespace) REFERENCES public.espacetravail(nomespace);


--
-- Name: administre administre_nomutilisateur_fkey; Type: FK CONSTRAINT; Schema: public; Owner: julienlarzul
--

ALTER TABLE ONLY public.administre
    ADD CONSTRAINT administre_nomutilisateur_fkey FOREIGN KEY (nomutilisateur) REFERENCES public.utilisateur(nomutilisateur);


--
-- Name: appartient appartient_nomespace_fkey; Type: FK CONSTRAINT; Schema: public; Owner: julienlarzul
--

ALTER TABLE ONLY public.appartient
    ADD CONSTRAINT appartient_nomespace_fkey FOREIGN KEY (nomespace) REFERENCES public.espacetravail(nomespace);


--
-- Name: appartient appartient_nomutilisateur_fkey; Type: FK CONSTRAINT; Schema: public; Owner: julienlarzul
--

ALTER TABLE ONLY public.appartient
    ADD CONSTRAINT appartient_nomutilisateur_fkey FOREIGN KEY (nomutilisateur) REFERENCES public.utilisateur(nomutilisateur);


--
-- Name: canal canal_nomespace_fkey; Type: FK CONSTRAINT; Schema: public; Owner: julienlarzul
--

ALTER TABLE ONLY public.canal
    ADD CONSTRAINT canal_nomespace_fkey FOREIGN KEY (nomespace) REFERENCES public.espacetravail(nomespace);


--
-- Name: invitationcanal invitationcanal_nomespace_fkey; Type: FK CONSTRAINT; Schema: public; Owner: julienlarzul
--

ALTER TABLE ONLY public.invitationcanal
    ADD CONSTRAINT invitationcanal_nomespace_fkey FOREIGN KEY (nomespace) REFERENCES public.espacetravail(nomespace);


--
-- Name: invitationcanal invitationcanal_nomutilisateur_fkey; Type: FK CONSTRAINT; Schema: public; Owner: julienlarzul
--

ALTER TABLE ONLY public.invitationcanal
    ADD CONSTRAINT invitationcanal_nomutilisateur_fkey FOREIGN KEY (nomutilisateur) REFERENCES public.utilisateur(nomutilisateur);


--
-- Name: invitationespace invitationespace_nomespace_fkey; Type: FK CONSTRAINT; Schema: public; Owner: julienlarzul
--

ALTER TABLE ONLY public.invitationespace
    ADD CONSTRAINT invitationespace_nomespace_fkey FOREIGN KEY (nomespace) REFERENCES public.espacetravail(nomespace);


--
-- Name: invitationespace invitationespace_nomutilisateur_fkey; Type: FK CONSTRAINT; Schema: public; Owner: julienlarzul
--

ALTER TABLE ONLY public.invitationespace
    ADD CONSTRAINT invitationespace_nomutilisateur_fkey FOREIGN KEY (nomutilisateur) REFERENCES public.utilisateur(nomutilisateur);


--
-- Name: message message_nomcanal_fkey; Type: FK CONSTRAINT; Schema: public; Owner: julienlarzul
--

ALTER TABLE ONLY public.message
    ADD CONSTRAINT message_nomcanal_fkey FOREIGN KEY (nomcanal) REFERENCES public.canal(nomcanal);


--
-- Name: message message_nomutilisateur_1_fkey; Type: FK CONSTRAINT; Schema: public; Owner: julienlarzul
--

ALTER TABLE ONLY public.message
    ADD CONSTRAINT message_nomutilisateur_1_fkey FOREIGN KEY (nomutilisateur_1) REFERENCES public.utilisateur(nomutilisateur);


--
-- Name: message message_nomutilisateur_2_fkey; Type: FK CONSTRAINT; Schema: public; Owner: julienlarzul
--

ALTER TABLE ONLY public.message
    ADD CONSTRAINT message_nomutilisateur_2_fkey FOREIGN KEY (nomutilisateur_2) REFERENCES public.utilisateur(nomutilisateur);


--
-- Name: message message_nomutilisateur_fkey; Type: FK CONSTRAINT; Schema: public; Owner: julienlarzul
--

ALTER TABLE ONLY public.message
    ADD CONSTRAINT message_nomutilisateur_fkey FOREIGN KEY (nomutilisateur) REFERENCES public.utilisateur(nomutilisateur);


--
-- Name: reaction reaction_idmessage_fkey; Type: FK CONSTRAINT; Schema: public; Owner: julienlarzul
--

ALTER TABLE ONLY public.reaction
    ADD CONSTRAINT reaction_idmessage_fkey FOREIGN KEY (idmessage) REFERENCES public.message(idmessage);


--
-- Name: reaction reaction_nomutilisateur_fkey; Type: FK CONSTRAINT; Schema: public; Owner: julienlarzul
--

ALTER TABLE ONLY public.reaction
    ADD CONSTRAINT reaction_nomutilisateur_fkey FOREIGN KEY (nomutilisateur) REFERENCES public.utilisateur(nomutilisateur);


--
-- PostgreSQL database dump complete
--

