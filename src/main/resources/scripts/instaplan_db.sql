-- This script initializes the instanplan db
DROP TABLE IF EXISTS cv_order_tbl;
DROP TABLE IF EXISTS inspection_report_tbl;

DROP TABLE IF EXISTS stripe_payment_details;
DROP TABLE IF EXISTS payment_details;
DROP TABLE IF EXISTS entity_user_tbl;
DROP TABLE IF EXISTS setting_user_tbl;

DROP TABLE IF EXISTS scan_tbl;
DROP TABLE IF EXISTS user_tbl;

DROP TABLE IF EXISTS setting_entity_tbl;
DROP TABLE IF EXISTS setting_role_tbl;
DROP TABLE IF EXISTS entity_tbl;
DROP TABLE IF EXISTS role_tbl;
DROP TABLE IF EXISTS setting_tbl;
DROP TABLE IF EXISTS super_entity_tbl;

DROP TABLE IF EXISTS alembic_version;

DROP TABLE IF EXISTS job_tbl;

------------------------ New Tables ---------------------------

CREATE TABLE IF NOT EXISTS cv_order_tbl
(
	cv_order_id 				SERIAL 		PRIMARY KEY,
	order_id 		 			varchar,
	streetAddress 	 			varchar,
	streetAddress2 	 			varchar,
	city 	 					varchar,
	state_c 	 				varchar,
	postalCode 					varchar,
	county 						varchar,
	propertyDataCollectorName 	varchar,
	propertyDataCollectorType 	varchar,
	userID 						varchar,
	firstName          			varchar,
	lastName 					varchar,
	companyName 				varchar,
	pdc_streetAddress 			varchar,
	pdc_city 					varchar,
	pdc_state 					varchar,
	pdc_postalCode    			varchar,
	pdc_phone  					varchar,
	pdc_email 					varchar,
	ip_user_id 					integer,
	status  					varchar(10),
	created_at	 				timestamp without time zone,
	modified_at 				timestamp without time zone
);

CREATE TABLE IF NOT EXISTS super_entity_tbl
(
	super_entity_id 	SERIAL 		PRIMARY KEY,
	super_entity_name 	varchar,
	super_entity_code 	varchar,
	description 		text,
	status  			varchar(10),
	created_at	 		timestamp without time zone,
	modified_at 		timestamp without time zone
);

CREATE TABLE IF NOT EXISTS role_tbl
(
	role_id 			SERIAL 		PRIMARY KEY,
	role_name 			varchar, -- display value
	role_code 			varchar, --[Appraiser,Homeowner,Residential, Architect,Commercial_Real_Estate_Agent,Construction,Interior_Designer]]
	description 		text, 	 -- AccountType=1(Non-Commercial)ApplePayments(In App): [Appraiser,Homeowner,Residential]
	account_type   		integer, --[1,2]AccountType=2(Commercial)Stripe: [Architect,Commercial_Real_Estate_Agent,Construction,Interior_Designer]
	role_type   		varchar, --[Commercial,Non_Commercial]
	status  			varchar(10),
	created_at	 		timestamp without time zone,
	modified_at 		timestamp without time zone
);

CREATE TABLE IF NOT EXISTS entity_tbl
(
	entity_id 			SERIAL 		PRIMARY KEY,
	entity_name 		varchar(60),
	entity_code  		varchar(60),
	enterprise_code		varchar, -- CLASS-VAL
	description 		text,
	entity_role_type    varchar(60), -- [Primary,Mixed]
	entity_role_id 		integer,
	super_entity_id 	integer,
	status  			varchar(10),
	created_at	 		timestamp without time zone,
	modified_at 		timestamp without time zone,
	CONSTRAINT fk_entity_super_entity_id
      FOREIGN KEY(super_entity_id) 
	  REFERENCES super_entity_tbl(super_entity_id),
	CONSTRAINT fk_entity_role_id
      FOREIGN KEY(entity_role_id) 
	  REFERENCES role_tbl(role_id)
);

CREATE TABLE IF NOT EXISTS setting_tbl
(
	setting_id 			SERIAL 		PRIMARY KEY,
	setting_name 		varchar, --Upload Images
	setting_code 		varchar, --Upload_Images
	setting_category 	varchar, --Upload
	description 		text,
	default_value 		varchar,
	is_enable			varchar, --YES/NO
	toggle				varchar, --ON/OFF
	is_display			varchar, --YES/NO
	status  			varchar(10),
	created_at	 		timestamp without time zone,
	modified_at 		timestamp without time zone
);

------------------------ Existing Tables ---------------------------
CREATE TABLE IF NOT EXISTS alembic_version
(
    version_num varchar(32) NOT NULL,
    CONSTRAINT alembic_version_pkc PRIMARY KEY (version_num)
);

CREATE TABLE IF NOT EXISTS user_tbl
(
    id 						SERIAL,
    name 					varchar(128),
    email 					varchar(128),
    password 				varchar(128),
    address 				varchar(255),
    profile_image 			varchar(255),
    phone_number 			varchar(255),
    created_at 				timestamp without time zone,
    modified_at 			timestamp without time zone,
    is_active 				integer,
    apple_token 			varchar(255),
    stripe_custid 			varchar(128),
    is_savecard_enabled 	integer,
    account_type 			integer,
    pmid 					varchar(128),
	storj_access_key 		varchar(128),
    storj_secret_key 		varchar(128),
    storj_prefix 			varchar(128),
	storj_revocable 		text,
	account_name 			varchar(60),
	
	bypass_inapp 			varchar(10),
	bypass_stripe 			varchar(10),
	super_entity 			varchar(60),
	super_entity_code 		varchar(60),
	company_name 			varchar(60),
	company_code 			varchar(60),
	company_id 				integer,
	cv_userid	 			integer, -- new
	entity_id 				integer, -- new
	role_id					integer, -- new
	
	enterprise_code			varchar, -- CLASS-VAL
	payment_type			varchar, -- PRE-PAID
	payment_method			varchar, -- ApplePay/Stripe 
	
    CONSTRAINT user_pkey PRIMARY KEY (id),
    CONSTRAINT user_email_key UNIQUE (email),
	
	CONSTRAINT fk_user_entity_id
      FOREIGN KEY(entity_id) 
	  REFERENCES entity_tbl(entity_id),
	CONSTRAINT fk_user_role_id
      FOREIGN KEY(role_id) 
	  REFERENCES role_tbl(role_id)
);

CREATE TABLE IF NOT EXISTS scan_tbl
(
    scan_id 					SERIAL,
    model_name 					text, 		-- not saving, need to remove
    scan_time 					varchar(50),
    dimensions 					text,
    two_d_pdf 					text, 		-- not saving, need to remove
    name 						varchar,
    address 					text,
    status 						varchar(100),
    owner_id 					integer NOT NULL,
    house_no 					varchar(20),
    flat 						varchar(120),
    latitude 					varchar(30),
    longitude 					varchar(30),
    constructed_year 			varchar(8), -- last saved on '2023-03-24'
    area_sqft 					varchar(20), -- last saved on '2023-03-24'
    model_url_3d 				varchar,
    model_url_2d 				varchar,
    image_url 					varchar(255), -- not saving, need to remove 
    description 				varchar(255),
    zipcode 					varchar(20),
    scan_type 					varchar(10),
    model_url_img 				varchar, --- zip/image url from storj = https://link.storjshare.io/jvxxeyhejguiei247jpxmx4vluua/neuron-dev-digiclone/scans/19/11-03-2023-17-25-07_19/11-03-2023-17-25-07_19_Img/RoomName/Images.zip
	room_name					varchar(128),
	feature 					varchar(30), -- default (scan), digi_clone

	grade_type 					varchar(10),
	grade_level 				varchar(10),
	house_type 					varchar(50),
	"structure" 				varchar(20),
	structure_type 				varchar(20),
	marker_file 				varchar(250),
	viewer_link 				varchar(250),
	system_description 			varchar(255),
	is_merged 					integer,
	merged_scan_ids 			text,
	is_moved 					integer, -- need to remove
    is_parent 					integer,
	base_scan_id 				varchar,
	session_name 				varchar,
	parents 					varchar,
	site_utility 				varchar, -- need to remove
	group_info 					varchar, -- need to remove
	
	room_photos 				text,
	property_use 	 			text,
	deficiencies 	 			text,
	updates 		 			text,
	features 		 			text,
	plumbing_fixture 			text,
	room_info					text,
	
	gia	 						NUMERIC(8,2),
	gla	 						NUMERIC(8,2),
	floor_livable_area			NUMERIC(8,2),
	floor_area	 				NUMERIC(8,2),
	finished_area	 			NUMERIC(8,2),
	unfinished_area	 			NUMERIC(8,2),
	total_area	 				NUMERIC(8,2),
	
	include_level_in_sq_ft_calc	integer,
	submitted_date_and_time 	timestamp without time zone,
	
	created_at 					timestamp without time zone,
    modified_at 				timestamp without time zone,
    CONSTRAINT scan_pkey PRIMARY KEY (scan_id),
	CONSTRAINT scan_owner_id_fkey 
		FOREIGN KEY(owner_id)
		REFERENCES user_tbl(id)
);

CREATE TABLE IF NOT EXISTS payment_details
(
    id 								integer,
    payment_id 						SERIAL,
    owner_id 						integer,
    original_id 					integer,
    status 							varchar(128),
    product 						varchar(128),
    product_id 						varchar(128),
    group_type 						varchar(128),
    price 							integer,
    app_item_id 					integer,
    bid 							varchar(128),
    environment 					varchar(128),
    offer_name 						varchar(128),
    renew_date 						timestamp without time zone,
    purchase_date 					timestamp without time zone,
    original_purchase_date 			timestamp without time zone,
    expire_date 					timestamp without time zone,
    transaction_id 					integer,
    original_transaction_id 		integer,
    quantity 						integer,
    content_version 				varchar(128),
    discounts 						text,
    subscription_period 			varchar(128),
    subscription_group_identifier 	varchar(128),
    introductory_price 				integer,
    address 						varchar(256),
	house_no 						varchar, -- newly added field
    latitude 						varchar(128),
    longitude 						varchar(128),
	created_at 						timestamp without time zone,
    modified_at 					timestamp without time zone,
    CONSTRAINT payment_details_pkey PRIMARY KEY (payment_id)
);

CREATE TABLE IF NOT EXISTS stripe_payment_details
(
    str_payment_id 					SERIAL 		PRIMARY KEY,
	scan_id 						integer,
	scan_time 						varchar(60),
    owner_id 						integer,
	stripe_custid 					varchar(60),
	setup_intent_id 				varchar(60),
	payment_intent_id 				varchar(60),
	payment_method_id 				varchar(60),
	price_f 						NUMERIC(5,2),
	address 						text,
    status 							varchar(128),
    transaction_id 					integer,
	created_at 						timestamp without time zone,
    modified_at 					timestamp without time zone
);

------------------------ New Tables ---------------------------

CREATE TABLE IF NOT EXISTS inspection_report_tbl
(
	report_id 			SERIAL 		PRIMARY KEY,
	user_id_i 			integer,
	address 			text,
	house_no  			varchar,
	"structure" 		varchar,
	structure_type 		varchar,
	pdapi 		 		text,
	location 	 		text,
	views  		 		text,
	updates 			text,
	deficiencies 		text,
	utilities 	 		text,
	status  			varchar(10),
	submitted_at 		timestamp without time zone,
	created_at	 		timestamp without time zone,
	modified_at 		timestamp without time zone,
	CONSTRAINT fk_inspection_report_user_id
      FOREIGN KEY(user_id_i) 
	  REFERENCES user_tbl(id)
);

CREATE TABLE IF NOT EXISTS setting_role_tbl
(
	setting_role_id 	SERIAL 		PRIMARY KEY,
	role_id 			integer, --Appriser
	setting_id 			integer,
	default_value 		varchar,
	is_enable			varchar, --YES/NO
	toggle				varchar, --ON/OFF
	is_display			varchar, --YES/NO
	status  			varchar(10),
	created_at	 		timestamp without time zone,
	modified_at 		timestamp without time zone,
	CONSTRAINT fk_setting_role_id
      FOREIGN KEY(role_id) 
	  REFERENCES role_tbl(role_id),
	CONSTRAINT fk_setting_role_setting_id
      FOREIGN KEY(setting_id) 
	  REFERENCES setting_tbl(setting_id)
);

CREATE TABLE IF NOT EXISTS setting_entity_tbl
(
	setting_entity_id 	SERIAL 		PRIMARY KEY,
	entity_id 			integer, --Appriser
	setting_id 			integer,
	default_value 		varchar,
	is_enable			varchar, --YES/NO
	toggle				varchar, --ON/OFF
	is_display			varchar, --YES/NO
	status  			varchar(10),
	created_at	 		timestamp without time zone,
	modified_at 		timestamp without time zone,
	CONSTRAINT fk_setting_entity_id
      FOREIGN KEY(entity_id) 
	  REFERENCES entity_tbl(entity_id),
	CONSTRAINT fk_setting_entity_setting_id
      FOREIGN KEY(setting_id) 
	  REFERENCES setting_tbl(setting_id)
);

CREATE TABLE IF NOT EXISTS setting_user_tbl
(
	setting_user_id 	SERIAL 		PRIMARY KEY,
	user_id_i 			integer, --1
	setting_id 			integer,
	default_value 		varchar,
	is_enable			varchar, --YES/NO
	toggle				varchar, --ON/OFF
	is_display			varchar, --YES/NO
	status  			varchar(10),
	created_at	 		timestamp without time zone,
	modified_at 		timestamp without time zone,
	CONSTRAINT fk_setting_user_id
      FOREIGN KEY(user_id_i) 
	  REFERENCES user_tbl(id),
	CONSTRAINT fk_setting_role_setting_id
      FOREIGN KEY(setting_id) 
	  REFERENCES setting_tbl(setting_id)
);

CREATE TABLE IF NOT EXISTS entity_user_tbl
(
	entity_user_id 		SERIAL 		PRIMARY KEY,
	entity_id           integer,
	entity_name         varchar,
	user_id_i 			integer,
	user_role_id		integer,
	user_email 			varchar(128),
	status  			varchar(10),
	created_at	 		timestamp without time zone,
	modified_at 		timestamp without time zone,
	CONSTRAINT fk_entity_user_entity_id
      FOREIGN KEY(entity_id) 
	  REFERENCES entity_tbl(entity_id),
	CONSTRAINT fk_entity_user_user_id
      FOREIGN KEY(user_id_i) 
	  REFERENCES user_tbl(id),
	CONSTRAINT fk_entity_user_role_id
      FOREIGN KEY(user_role_id) 
	  REFERENCES role_tbl(role_id)
);

----------------------------------
CREATE TABLE IF NOT EXISTS job_tbl
(
	job_id 						SERIAL 		PRIMARY KEY,
	job_name 					varchar,
	job_status 					varchar,
	filename 					varchar,
	super_entity_name 			varchar,
	entity_name 				varchar,
	folder_path 				text,
	address 	 				varchar,
	house_no 					varchar,
	super_entity_id 			integer,
	entity_id  					integer,
	user_id_i 					integer,
	status  					varchar(10),
	submitted_at 				timestamp without time zone,
	created_at	 				timestamp without time zone,
	modified_at 				timestamp without time zone
);
