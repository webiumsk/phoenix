CREATE TABLE PaymentMeta (id TEXT NOT NULL PRIMARY KEY, swap_in_address TEXT DEFAULT NULL, swap_in_tx TEXT DEFAULT NULL, swap_out_address TEXT DEFAULT NULL, swap_out_tx TEXT DEFAULT NULL, swap_out_feerate_per_byte INTEGER DEFAULT NULL, swap_out_fee_sat INTEGER DEFAULT NULL, swap_out_conf INTEGER DEFAULT NULL, funding_tx TEXT DEFAULT NULL, funding_fee_pct REAL DEFAULT NULL, funding_fee_raw_sat INTEGER DEFAULT NULL, closing_type INTEGER DEFAULT NULL, closing_channel_id TEXT UNIQUE DEFAULT NULL, closing_spending_txs TEXT DEFAULT NULL, closing_main_output_script TEXT DEFAULT NULL, closing_cause TEXT DEFAULT NULL, custom_desc TEXT DEFAULT NULL, lnurlpay_url TEXT DEFAULT NULL, lnurlpay_action_typeversion TEXT DEFAULT NULL, lnurlpay_action_data TEXT DEFAULT NULL, lnurlpay_meta_description TEXT DEFAULT NULL, lnurlpay_meta_long_description TEXT DEFAULT NULL, lnurlpay_meta_identifier TEXT DEFAULT NULL, lnurlpay_meta_email TEXT DEFAULT NULL);
CREATE INDEX IF NOT EXISTS payment_closing_channel_id_index ON PaymentMeta(closing_channel_id);

CREATE TABLE PayToOpenMeta (id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, payment_hash TEXT NOT NULL, fee_sat INTEGER NOT NULL, amount_sat INTEGER NOT NULL, capacity_sat INTEGER NOT NULL, timestamp INTEGER NOT NULL);
CREATE INDEX IF NOT EXISTS paytoopen_payment_hash_index ON PayToOpenMeta(payment_hash);

INSERT INTO "PaymentMeta" ("id", "swap_in_address", "swap_in_tx", "swap_out_address", "swap_out_tx", "swap_out_feerate_per_byte", "swap_out_fee_sat", "swap_out_conf", "funding_tx", "funding_fee_pct", "funding_fee_raw_sat", "closing_type", "closing_channel_id", "closing_spending_txs", "closing_main_output_script", "closing_cause", "custom_desc", "lnurlpay_url", "lnurlpay_action_typeversion", "lnurlpay_action_data", "lnurlpay_meta_description", "lnurlpay_meta_long_description", "lnurlpay_meta_identifier", "lnurlpay_meta_email") VALUES ('ce87d86556cc7a1ed31fdfe2ade49a97506f29eda69c28df6d3ca0143324201b', 'tb1qwq05evgh9pugurpthes5wld2nuu5f2s7u9pt2q', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO "PaymentMeta" ("id", "swap_in_address", "swap_in_tx", "swap_out_address", "swap_out_tx", "swap_out_feerate_per_byte", "swap_out_fee_sat", "swap_out_conf", "funding_tx", "funding_fee_pct", "funding_fee_raw_sat", "closing_type", "closing_channel_id", "closing_spending_txs", "closing_main_output_script", "closing_cause", "custom_desc", "lnurlpay_url", "lnurlpay_action_typeversion", "lnurlpay_action_data", "lnurlpay_meta_description", "lnurlpay_meta_long_description", "lnurlpay_meta_identifier", "lnurlpay_meta_email") VALUES ('9d4feb0e-eb57-4eae-a982-5e3f39f4126f', NULL, NULL, '2N1sjnTPsAaG3oGMHTHonANbHEERuiqN6k6', NULL, '3', '2840', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO "PaymentMeta" ("id", "swap_in_address", "swap_in_tx", "swap_out_address", "swap_out_tx", "swap_out_feerate_per_byte", "swap_out_fee_sat", "swap_out_conf", "funding_tx", "funding_fee_pct", "funding_fee_raw_sat", "closing_type", "closing_channel_id", "closing_spending_txs", "closing_main_output_script", "closing_cause", "custom_desc", "lnurlpay_url", "lnurlpay_action_typeversion", "lnurlpay_action_data", "lnurlpay_meta_description", "lnurlpay_meta_long_description", "lnurlpay_meta_identifier", "lnurlpay_meta_email") VALUES ('789f1fa67bbf709bef70fb9b86dae3362635f7d35c59066a6c63e6a44f20bb1c', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'description ajoutée après 😊', NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO "PaymentMeta" ("id", "swap_in_address", "swap_in_tx", "swap_out_address", "swap_out_tx", "swap_out_feerate_per_byte", "swap_out_fee_sat", "swap_out_conf", "funding_tx", "funding_fee_pct", "funding_fee_raw_sat", "closing_type", "closing_channel_id", "closing_spending_txs", "closing_main_output_script", "closing_cause", "custom_desc", "lnurlpay_url", "lnurlpay_action_typeversion", "lnurlpay_action_data", "lnurlpay_meta_description", "lnurlpay_meta_long_description", "lnurlpay_meta_identifier", "lnurlpay_meta_email") VALUES ('a7f837c1-0e8d-434c-8a12-d68780f2c0d0', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '3749642f6caa1a13a9026f966eb13bd5a970ee237fb173d78602b2b31b7bc804', '24893dcd47403242e86e86344acfe69042bb5c1466df11cf43696fad7d29dfe3', '2NBPdqEiX2Wb9VNTqNBXBjgCAnHvhBD8sc3', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);

INSERT INTO "PayToOpenMeta" ("id", "payment_hash", "fee_sat", "amount_sat", "capacity_sat", "timestamp") VALUES ('1', '2927d1da0eb4c79ec55806cb7d56234876f07ed389a3d23985fed3c82235aa88', '3000', '60000', '193000', '1656341949220');
INSERT INTO "PayToOpenMeta" ("id", "payment_hash", "fee_sat", "amount_sat", "capacity_sat", "timestamp") VALUES ('2', '3baedbba9f06940a87e2f38a22f5ce72302cabc0ef5f457fdad45c58d0c3e928', '5678', '567890', '957513', '1656403752445');