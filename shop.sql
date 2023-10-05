--
-- PostgreSQL database dump
--

-- Dumped from database version 16.0
-- Dumped by pg_dump version 16.0

-- Started on 2023-10-05 14:45:21

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

--
-- TOC entry 4810 (class 1262 OID 16396)
-- Name: shop; Type: DATABASE; Schema: -; Owner: postgres
--

CREATE DATABASE shop WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'Russian_Russia.1251';


ALTER DATABASE shop OWNER TO postgres;

\connect shop

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

--
-- TOC entry 232 (class 1255 OID 24768)
-- Name: generate_purchases(integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.generate_purchases(count_purchases integer) RETURNS character
    LANGUAGE plpgsql
    AS $_$
DECLARE
    count INT;
BEGIN
    count = 0;
    
    IF NOT EXISTS(SELECT id_purchase FROM purchases WHERE id_purchase = 1) THEN
        INSERT INTO purchases (id_customer, id_product, datePurchase)
        SELECT
            (SELECT id_customer FROM customers OFFSET floor(random() * (SELECT COUNT(*) FROM customers)) LIMIT 1),
            (SELECT id_product FROM products OFFSET floor(random() * (SELECT COUNT(*) FROM products)) LIMIT 1),
            CURRENT_DATE - (random() * 365)::INTEGER
        FROM generate_series(1, $1);
    END IF;

    WHILE count < count_purchases LOOP
        count = count + 1;
        UPDATE purchases SET id_customer = floor(random() * 20) + 1, id_product = floor(random() * 20) + 1
        WHERE id_purchase = count;
    END LOOP;

    RETURN 'Done';
END;
$_$;


ALTER FUNCTION public.generate_purchases(count_purchases integer) OWNER TO postgres;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 216 (class 1259 OID 24595)
-- Name: customers; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.customers (
    id_customer integer NOT NULL,
    firstname character varying(255) NOT NULL,
    lastname character varying(255) NOT NULL
);


ALTER TABLE public.customers OWNER TO postgres;

--
-- TOC entry 215 (class 1259 OID 24594)
-- Name: customers_id_customer_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.customers_id_customer_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.customers_id_customer_seq OWNER TO postgres;

--
-- TOC entry 4811 (class 0 OID 0)
-- Dependencies: 215
-- Name: customers_id_customer_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.customers_id_customer_seq OWNED BY public.customers.id_customer;


--
-- TOC entry 218 (class 1259 OID 24604)
-- Name: products; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.products (
    id_product integer NOT NULL,
    name character varying(255) NOT NULL,
    expenses numeric NOT NULL
);


ALTER TABLE public.products OWNER TO postgres;

--
-- TOC entry 217 (class 1259 OID 24603)
-- Name: products_id_product_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.products_id_product_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.products_id_product_seq OWNER TO postgres;

--
-- TOC entry 4812 (class 0 OID 0)
-- Dependencies: 217
-- Name: products_id_product_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.products_id_product_seq OWNED BY public.products.id_product;


--
-- TOC entry 220 (class 1259 OID 24613)
-- Name: purchases; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.purchases (
    id_purchase integer NOT NULL,
    id_customer integer NOT NULL,
    id_product integer NOT NULL,
    datepurchase date NOT NULL
);


ALTER TABLE public.purchases OWNER TO postgres;

--
-- TOC entry 219 (class 1259 OID 24612)
-- Name: purchases_id_purchase_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.purchases_id_purchase_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.purchases_id_purchase_seq OWNER TO postgres;

--
-- TOC entry 4813 (class 0 OID 0)
-- Dependencies: 219
-- Name: purchases_id_purchase_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.purchases_id_purchase_seq OWNED BY public.purchases.id_purchase;


--
-- TOC entry 4645 (class 2604 OID 24598)
-- Name: customers id_customer; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.customers ALTER COLUMN id_customer SET DEFAULT nextval('public.customers_id_customer_seq'::regclass);


--
-- TOC entry 4646 (class 2604 OID 24607)
-- Name: products id_product; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.products ALTER COLUMN id_product SET DEFAULT nextval('public.products_id_product_seq'::regclass);


--
-- TOC entry 4647 (class 2604 OID 24616)
-- Name: purchases id_purchase; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.purchases ALTER COLUMN id_purchase SET DEFAULT nextval('public.purchases_id_purchase_seq'::regclass);


--
-- TOC entry 4800 (class 0 OID 24595)
-- Dependencies: 216
-- Data for Name: customers; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.customers VALUES (1, 'Иван', 'Иванов');
INSERT INTO public.customers VALUES (2, 'Мария', 'Петрова');
INSERT INTO public.customers VALUES (3, 'Андрей', 'Сидоров');
INSERT INTO public.customers VALUES (4, 'Екатерина', 'Смирнова');
INSERT INTO public.customers VALUES (5, 'Павел', 'Козлов');
INSERT INTO public.customers VALUES (6, 'Ольга', 'Морозова');
INSERT INTO public.customers VALUES (7, 'Сергей', 'Ковалев');
INSERT INTO public.customers VALUES (8, 'Анна', 'Новикова');
INSERT INTO public.customers VALUES (9, 'Дмитрий', 'Попов');
INSERT INTO public.customers VALUES (10, 'Татьяна', 'Максимова');
INSERT INTO public.customers VALUES (11, 'Алексей', 'Алексеев');
INSERT INTO public.customers VALUES (12, 'Наталья', 'Соловьева');
INSERT INTO public.customers VALUES (13, 'Артем', 'Волков');
INSERT INTO public.customers VALUES (14, 'Елена', 'Исакова');
INSERT INTO public.customers VALUES (15, 'Илья', 'Козлов');
INSERT INTO public.customers VALUES (16, 'Светлана', 'Павлова');
INSERT INTO public.customers VALUES (17, 'Владимир', 'Горбачев');
INSERT INTO public.customers VALUES (18, 'Юлия', 'Беляева');
INSERT INTO public.customers VALUES (19, 'Григорий', 'Медведев');
INSERT INTO public.customers VALUES (20, 'Марина', 'Тимофеева');


--
-- TOC entry 4802 (class 0 OID 24604)
-- Dependencies: 218
-- Data for Name: products; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.products VALUES (1, 'Хлеб', 100);
INSERT INTO public.products VALUES (2, 'Молоко', 50);
INSERT INTO public.products VALUES (3, 'Яйца', 30);
INSERT INTO public.products VALUES (4, 'Сахар', 40);
INSERT INTO public.products VALUES (5, 'Масло', 120);
INSERT INTO public.products VALUES (6, 'Картофель', 70);
INSERT INTO public.products VALUES (7, 'Мясо', 500);
INSERT INTO public.products VALUES (8, 'Рис', 80);
INSERT INTO public.products VALUES (9, 'Макароны', 60);
INSERT INTO public.products VALUES (10, 'Кофе', 150);
INSERT INTO public.products VALUES (11, 'Чай', 70);
INSERT INTO public.products VALUES (12, 'Сок', 90);
INSERT INTO public.products VALUES (13, 'Печенье', 45);
INSERT INTO public.products VALUES (14, 'Колбаса', 200);
INSERT INTO public.products VALUES (15, 'Сыр', 180);
INSERT INTO public.products VALUES (16, 'Орехи', 130);
INSERT INTO public.products VALUES (17, 'Вода', 20);
INSERT INTO public.products VALUES (18, 'Суп', 75);
INSERT INTO public.products VALUES (19, 'Пельмени', 110);
INSERT INTO public.products VALUES (20, 'Шоколад', 95);


--
-- TOC entry 4804 (class 0 OID 24613)
-- Dependencies: 220
-- Data for Name: purchases; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.purchases VALUES (1, 9, 8, '2023-04-04');
INSERT INTO public.purchases VALUES (2, 19, 17, '2023-06-26');
INSERT INTO public.purchases VALUES (3, 17, 13, '2023-08-07');
INSERT INTO public.purchases VALUES (4, 18, 8, '2023-02-18');
INSERT INTO public.purchases VALUES (5, 7, 5, '2023-09-21');
INSERT INTO public.purchases VALUES (6, 2, 9, '2023-07-11');
INSERT INTO public.purchases VALUES (7, 17, 1, '2023-07-29');
INSERT INTO public.purchases VALUES (8, 13, 5, '2023-04-01');
INSERT INTO public.purchases VALUES (9, 20, 7, '2023-05-28');
INSERT INTO public.purchases VALUES (10, 17, 9, '2022-10-17');
INSERT INTO public.purchases VALUES (11, 12, 20, '2023-07-30');
INSERT INTO public.purchases VALUES (12, 20, 3, '2022-10-17');
INSERT INTO public.purchases VALUES (13, 11, 4, '2022-12-15');
INSERT INTO public.purchases VALUES (14, 18, 12, '2023-07-20');
INSERT INTO public.purchases VALUES (15, 18, 17, '2023-01-05');
INSERT INTO public.purchases VALUES (16, 9, 4, '2023-06-08');
INSERT INTO public.purchases VALUES (17, 13, 8, '2023-05-15');
INSERT INTO public.purchases VALUES (18, 14, 11, '2022-10-30');
INSERT INTO public.purchases VALUES (19, 12, 6, '2022-12-05');
INSERT INTO public.purchases VALUES (20, 15, 6, '2022-11-07');
INSERT INTO public.purchases VALUES (21, 15, 2, '2023-08-19');
INSERT INTO public.purchases VALUES (22, 13, 3, '2023-08-24');
INSERT INTO public.purchases VALUES (23, 20, 6, '2023-02-15');
INSERT INTO public.purchases VALUES (24, 11, 7, '2023-03-11');
INSERT INTO public.purchases VALUES (25, 18, 4, '2022-10-25');
INSERT INTO public.purchases VALUES (26, 9, 14, '2022-11-05');
INSERT INTO public.purchases VALUES (27, 8, 1, '2023-01-14');
INSERT INTO public.purchases VALUES (28, 8, 9, '2023-02-12');
INSERT INTO public.purchases VALUES (29, 10, 3, '2022-12-27');
INSERT INTO public.purchases VALUES (30, 13, 14, '2022-12-15');
INSERT INTO public.purchases VALUES (31, 7, 13, '2022-12-18');
INSERT INTO public.purchases VALUES (32, 9, 20, '2023-01-08');
INSERT INTO public.purchases VALUES (33, 15, 7, '2023-09-02');
INSERT INTO public.purchases VALUES (34, 16, 15, '2023-05-21');
INSERT INTO public.purchases VALUES (35, 4, 19, '2023-06-23');
INSERT INTO public.purchases VALUES (36, 3, 19, '2023-05-11');
INSERT INTO public.purchases VALUES (37, 3, 19, '2022-10-27');
INSERT INTO public.purchases VALUES (38, 4, 19, '2023-06-12');
INSERT INTO public.purchases VALUES (39, 15, 14, '2023-09-23');
INSERT INTO public.purchases VALUES (40, 17, 9, '2023-06-14');
INSERT INTO public.purchases VALUES (41, 20, 14, '2022-10-26');
INSERT INTO public.purchases VALUES (42, 1, 4, '2023-07-09');
INSERT INTO public.purchases VALUES (43, 11, 8, '2023-07-01');
INSERT INTO public.purchases VALUES (44, 20, 20, '2022-10-31');
INSERT INTO public.purchases VALUES (45, 10, 1, '2023-05-22');
INSERT INTO public.purchases VALUES (46, 18, 17, '2023-07-04');
INSERT INTO public.purchases VALUES (47, 5, 1, '2022-12-15');
INSERT INTO public.purchases VALUES (48, 16, 15, '2023-04-08');
INSERT INTO public.purchases VALUES (49, 6, 5, '2023-05-30');
INSERT INTO public.purchases VALUES (50, 18, 15, '2022-11-03');
INSERT INTO public.purchases VALUES (51, 15, 8, '2023-09-21');
INSERT INTO public.purchases VALUES (52, 10, 7, '2023-01-14');
INSERT INTO public.purchases VALUES (53, 3, 7, '2022-11-13');
INSERT INTO public.purchases VALUES (54, 11, 19, '2023-08-20');
INSERT INTO public.purchases VALUES (55, 17, 4, '2023-09-27');
INSERT INTO public.purchases VALUES (56, 9, 14, '2023-09-19');
INSERT INTO public.purchases VALUES (57, 15, 14, '2023-09-24');
INSERT INTO public.purchases VALUES (58, 18, 15, '2023-07-06');
INSERT INTO public.purchases VALUES (59, 15, 15, '2023-08-18');
INSERT INTO public.purchases VALUES (60, 10, 12, '2023-08-10');
INSERT INTO public.purchases VALUES (61, 5, 5, '2022-10-26');
INSERT INTO public.purchases VALUES (62, 13, 15, '2023-06-02');
INSERT INTO public.purchases VALUES (63, 15, 7, '2023-03-06');
INSERT INTO public.purchases VALUES (64, 12, 14, '2023-09-26');
INSERT INTO public.purchases VALUES (65, 17, 9, '2022-12-27');
INSERT INTO public.purchases VALUES (66, 15, 16, '2023-07-06');
INSERT INTO public.purchases VALUES (67, 4, 16, '2023-08-24');
INSERT INTO public.purchases VALUES (68, 19, 11, '2023-01-01');
INSERT INTO public.purchases VALUES (69, 9, 9, '2023-03-26');
INSERT INTO public.purchases VALUES (70, 13, 16, '2022-12-26');
INSERT INTO public.purchases VALUES (71, 7, 1, '2023-07-15');
INSERT INTO public.purchases VALUES (72, 13, 7, '2022-10-05');
INSERT INTO public.purchases VALUES (73, 7, 11, '2023-05-24');
INSERT INTO public.purchases VALUES (74, 5, 14, '2023-08-15');
INSERT INTO public.purchases VALUES (75, 10, 8, '2023-05-29');
INSERT INTO public.purchases VALUES (76, 9, 13, '2023-07-13');
INSERT INTO public.purchases VALUES (77, 13, 3, '2023-05-19');
INSERT INTO public.purchases VALUES (78, 4, 8, '2023-05-18');
INSERT INTO public.purchases VALUES (79, 8, 1, '2022-11-11');
INSERT INTO public.purchases VALUES (80, 13, 14, '2022-10-14');
INSERT INTO public.purchases VALUES (81, 6, 20, '2022-10-14');
INSERT INTO public.purchases VALUES (82, 18, 6, '2023-07-06');
INSERT INTO public.purchases VALUES (83, 15, 19, '2023-07-17');
INSERT INTO public.purchases VALUES (84, 5, 3, '2023-08-29');
INSERT INTO public.purchases VALUES (85, 17, 7, '2022-12-12');
INSERT INTO public.purchases VALUES (86, 9, 10, '2023-08-10');
INSERT INTO public.purchases VALUES (87, 1, 7, '2022-11-01');
INSERT INTO public.purchases VALUES (88, 13, 12, '2023-02-12');
INSERT INTO public.purchases VALUES (89, 1, 15, '2023-06-06');
INSERT INTO public.purchases VALUES (90, 10, 7, '2023-09-01');
INSERT INTO public.purchases VALUES (91, 1, 1, '2023-05-03');
INSERT INTO public.purchases VALUES (92, 15, 11, '2023-03-18');
INSERT INTO public.purchases VALUES (93, 4, 11, '2022-11-17');
INSERT INTO public.purchases VALUES (94, 5, 12, '2023-06-18');
INSERT INTO public.purchases VALUES (95, 5, 18, '2023-07-18');
INSERT INTO public.purchases VALUES (96, 6, 15, '2023-05-02');
INSERT INTO public.purchases VALUES (97, 10, 19, '2023-01-22');
INSERT INTO public.purchases VALUES (98, 12, 4, '2023-09-30');
INSERT INTO public.purchases VALUES (99, 10, 10, '2022-11-27');
INSERT INTO public.purchases VALUES (100, 4, 2, '2022-11-12');


--
-- TOC entry 4814 (class 0 OID 0)
-- Dependencies: 215
-- Name: customers_id_customer_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.customers_id_customer_seq', 20, true);


--
-- TOC entry 4815 (class 0 OID 0)
-- Dependencies: 217
-- Name: products_id_product_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.products_id_product_seq', 20, true);


--
-- TOC entry 4816 (class 0 OID 0)
-- Dependencies: 219
-- Name: purchases_id_purchase_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.purchases_id_purchase_seq', 100, true);


--
-- TOC entry 4649 (class 2606 OID 24602)
-- Name: customers customers_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.customers
    ADD CONSTRAINT customers_pkey PRIMARY KEY (id_customer);


--
-- TOC entry 4651 (class 2606 OID 24611)
-- Name: products products_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.products
    ADD CONSTRAINT products_pkey PRIMARY KEY (id_product);


--
-- TOC entry 4653 (class 2606 OID 24618)
-- Name: purchases purchases_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.purchases
    ADD CONSTRAINT purchases_pkey PRIMARY KEY (id_purchase);


--
-- TOC entry 4654 (class 2606 OID 24619)
-- Name: purchases purchases_id_customer_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.purchases
    ADD CONSTRAINT purchases_id_customer_fkey FOREIGN KEY (id_customer) REFERENCES public.customers(id_customer) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 4655 (class 2606 OID 24624)
-- Name: purchases purchases_id_product_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.purchases
    ADD CONSTRAINT purchases_id_product_fkey FOREIGN KEY (id_product) REFERENCES public.products(id_product) ON UPDATE CASCADE ON DELETE CASCADE;


-- Completed on 2023-10-05 14:45:21

--
-- PostgreSQL database dump complete
--

