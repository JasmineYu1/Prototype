--
-- PostgreSQL database dump
--

-- Dumped from database version 16.2
-- Dumped by pg_dump version 16.2

-- Started on 2024-12-06 15:10:07 GMT

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
-- TOC entry 215 (class 1259 OID 16425)
-- Name: resident_presence; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.resident_presence (
    day character varying(3),
    hour character varying(5),
    user_id character varying(10),
    count integer,
    week integer
);


ALTER TABLE public.resident_presence OWNER TO postgres;

--
-- TOC entry 3585 (class 0 OID 16425)
-- Dependencies: 215
-- Data for Name: resident_presence; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.resident_presence (day, hour, user_id, count, week) FROM stdin;
\.


-- Completed on 2024-12-06 15:10:07 GMT

--
-- PostgreSQL database dump complete
--

