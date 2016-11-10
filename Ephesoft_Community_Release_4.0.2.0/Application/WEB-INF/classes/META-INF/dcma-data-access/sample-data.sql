



/*Document Types*/
INSERT INTO document_type(id, creation_date, last_modified, document_type_description, min_confidence_threshold, document_type_name, priority, batch_class_id) VALUES (1,now(),now(),'Unknown',0,'Unknown',0,1);
INSERT INTO document_type(id, creation_date, last_modified, document_type_description, min_confidence_threshold, document_type_name, priority, batch_class_id) VALUES (2,now(),now(),'Application-Checklist',35,'Application-Checklist',0,1);
INSERT INTO document_type(id, creation_date, last_modified, document_type_description, min_confidence_threshold, document_type_name, priority, batch_class_id) VALUES (3,now(),now(),'Workers-Comp-02',35,'Workers-Comp-02',0,1);
INSERT INTO document_type(id, creation_date, last_modified, document_type_description, min_confidence_threshold, document_type_name, priority, batch_class_id) VALUES (4,now(),now(),'US-Invoice-Data',35,'US-Invoice-Data',0,1);
INSERT INTO document_type(id, creation_date, last_modified, document_type_description, min_confidence_threshold, document_type_name, priority, batch_class_id) VALUES (5,now(),now(),'Unknown',0,'Unknown',0,2);
INSERT INTO document_type(id, creation_date, last_modified, document_type_description, min_confidence_threshold, document_type_name, priority, batch_class_id) VALUES (6,now(),now(),'Application-Checklist',35,'Application-Checklist',0,2);
INSERT INTO document_type(id, creation_date, last_modified, document_type_description, min_confidence_threshold, document_type_name, priority, batch_class_id) VALUES (7,now(),now(),'Workers-Comp-02',35,'Workers-Comp-02',0,2);
INSERT INTO document_type(id, creation_date, last_modified, document_type_description, min_confidence_threshold, document_type_name, priority, batch_class_id) VALUES (8,now(),now(),'US-Invoice-Data',35,'US-Invoice-Data',0,2);
INSERT INTO document_type(id, creation_date, last_modified, document_type_description, min_confidence_threshold, document_type_name, priority, batch_class_id) VALUES (9,now(),now(),'Invoice-Table',35,'Invoice-Table',0,1);
INSERT INTO document_type(id, creation_date, last_modified, document_type_description, min_confidence_threshold, document_type_name, priority, batch_class_id) VALUES (10,now(),now(),'Invoice-Table',35,'Invoice-Table',0,2);
INSERT INTO document_type(id, creation_date, last_modified, document_type_description, min_confidence_threshold, document_type_name, priority, batch_class_id) VALUES (11,now(),now(),'US Invoice',35,'US Invoice',0,1);
INSERT INTO document_type(id, creation_date, last_modified, document_type_description, min_confidence_threshold, document_type_name, priority, batch_class_id) VALUES (12,now(),now(),'US Invoice',35,'US Invoice',0,2);
INSERT INTO document_type(id, creation_date, last_modified, document_type_description, min_confidence_threshold, document_type_name, priority, batch_class_id) VALUES (13,now(),now(),'Voting Pharmacy',35,'Voting_Pharmacy',0,1);
INSERT INTO document_type(id, creation_date, last_modified, document_type_description, min_confidence_threshold, document_type_name, priority, batch_class_id) VALUES (14,now(),now(),'Voting Machine',35,'Voting_Machine',0,1);
INSERT INTO document_type(id, creation_date, last_modified, document_type_description, min_confidence_threshold, document_type_name, priority, batch_class_id) VALUES (15,now(),now(),'Searchable Document',35,'Searchable Document',0,3);
INSERT INTO document_type(id, creation_date, last_modified, document_type_description, min_confidence_threshold, document_type_name, priority, batch_class_id) VALUES (16,now(),now(),'Unknown',0,'Unknown',0,4);
INSERT INTO document_type(id, creation_date, last_modified, document_type_description, min_confidence_threshold, document_type_name, priority, batch_class_id) VALUES (17,now(),now(),'Application-Checklist',35,'Application-Checklist',0,4);
INSERT INTO document_type(id, creation_date, last_modified, document_type_description, min_confidence_threshold, document_type_name, priority, batch_class_id) VALUES (18,now(),now(),'Workers-Comp-02',35,'Workers-Comp-02',0,4);
INSERT INTO document_type(id, creation_date, last_modified, document_type_description, min_confidence_threshold, document_type_name, priority, batch_class_id) VALUES (19,now(),now(),'US-Invoice-Data',35,'US-Invoice-Data',0,4);

 
/*Field Types*/
INSERT INTO field_type(id, creation_date, last_modified, field_data_type, field_type_description, field_type_name, field_order_number, pattern, sample_value, document_type_id) VALUES (1,now(),now(),'STRING','Invoice Date','Invoice Date', 1, 'Invoice;Date:;[0-9]{2}/[0-9]{2}/[0-9]{2,4}', 'dd/mm/yyyy', 6);
INSERT INTO field_type(id, creation_date, last_modified, field_data_type, field_type_description, field_type_name, field_order_number, pattern, sample_value, document_type_id) VALUES (2,now(),now(),'STRING','Part Number','Part Number', 2, '998[0-9]{9}', '998xxxxxxxxx', 6);
INSERT INTO field_type(id, creation_date, last_modified, field_data_type, field_type_description, field_type_name, field_order_number, pattern, sample_value, document_type_id) VALUES (3,now(),now(),'STRING','Invoice Total','Invoice Total', 3, '!Sub;Total;[0-9]{1,3},?[0-9]{1,3}\\S[0-9]{2}','wx.yz',6);
INSERT INTO field_type(id, creation_date, last_modified, field_data_type, field_type_description, field_type_name, field_order_number, pattern, sample_value, document_type_id) VALUES (4,now(),now(),'STRING','State','State', 4, 'CA|NY|AZ|NV|NJ','CA|NY|AZ|NV|NJ',6);
INSERT INTO field_type(id, creation_date, last_modified, field_data_type, field_type_description, field_type_name, field_order_number, pattern, sample_value, document_type_id) VALUES (5,now(),now(),'STRING','City','City', 5, 'Irvine|Paris|Sydney','Irvine|Paris|Sydney' ,6);
INSERT INTO field_type(id, creation_date, last_modified, field_data_type, field_type_description, field_type_name, field_order_number, pattern, sample_value, document_type_id) VALUES (6,now(),now(),'STRING','Invoice Date','Invoice Date', 1, 'Invoice;Date:;[0-9]{2}/[0-9]{2}/[0-9]{2,4}','dd/mm/yyyy' ,7);
INSERT INTO field_type(id, creation_date, last_modified, field_data_type, field_type_description, field_type_name, field_order_number, pattern, sample_value, document_type_id) VALUES (7,now(),now(),'STRING','Part Number','Part Number', 2, '998[0-9]{9}','998xxxxxxxxx', 7);
INSERT INTO field_type(id, creation_date, last_modified, field_data_type, field_type_description, field_type_name, field_order_number, pattern, sample_value, document_type_id) VALUES (8,now(),now(),'STRING','Invoice Total','Invoice Total', 3, '!Sub;Total;[0-9]{1,3},?[0-9]{1,3}\\S[0-9]{2}','wx.yz',7);
INSERT INTO field_type(id, creation_date, last_modified, field_data_type, field_type_description, field_type_name, field_order_number, pattern, sample_value, document_type_id) VALUES (9,now(),now(),'STRING','State','State', 4, 'CA|NY|AZ|NV|NJ','CA|NY|AZ|NV|NJ' ,7);
INSERT INTO field_type(id, creation_date, last_modified, field_data_type, field_type_description, field_type_name, field_order_number, pattern, sample_value, document_type_id) VALUES (10,now(),now(),'STRING','City','City', 5, 'Irvine|Paris|Sydney','Irvine|Paris|Sydney',7);
INSERT INTO field_type(id, creation_date, last_modified, field_data_type, field_type_description, field_type_name, field_order_number, pattern, sample_value, document_type_id) VALUES (11,now(),now(),'STRING','Invoice Date','Invoice Date', 1, 'Invoice;Date:;[0-9]{2}/[0-9]{2}/[0-9]{2,4}','dd/mm/yyyy',8);
INSERT INTO field_type(id, creation_date, last_modified, field_data_type, field_type_description, field_type_name, field_order_number, pattern, sample_value, document_type_id) VALUES (12,now(),now(),'STRING','Part Number','Part Number', 2, '998[0-9]{9}','998xxxxxxxxx',8);
INSERT INTO field_type(id, creation_date, last_modified, field_data_type, field_type_description, field_type_name, field_order_number, pattern, sample_value, document_type_id) VALUES (13,now(),now(),'STRING','Invoice Total','Invoice Total', 3, '!Sub;Total;[0-9]{1,3},?[0-9]{1,3}\\S[0-9]{2}','wx.yz',8);
INSERT INTO field_type(id, creation_date, last_modified, field_data_type, field_type_description, field_type_name, field_order_number, pattern, sample_value, document_type_id) VALUES (14,now(),now(),'STRING','State','State', 4, 'CA|NY|AZ|NV|NJ','CA|NY|AZ|NV|NJ',8);
INSERT INTO field_type(id, creation_date, last_modified, field_data_type, field_type_description, field_type_name, field_order_number, pattern, sample_value, document_type_id) VALUES (15,now(),now(),'STRING','City','City', 5, 'Irvine|Paris|Sydney','Irvine|Paris|Sydney' ,8);
INSERT INTO field_type(id, creation_date, last_modified, field_data_type, field_type_description, field_type_name, field_order_number, pattern, sample_value, document_type_id) VALUES (16,now(),now(),'STRING','Invoice Date','Invoice Date', 1, 'Invoice;Date:;[0-9]{2}/[0-9]{2}/[0-9]{2,4}','dd/mm/yyyy',2);
INSERT INTO field_type(id, creation_date, last_modified, field_data_type, field_type_description, field_type_name, field_order_number, pattern, sample_value, document_type_id) VALUES (17,now(),now(),'STRING','Part Number','Part Number', 2, '998[0-9]{9}','998xxxxxxxxx',2);
INSERT INTO field_type(id, creation_date, last_modified, field_data_type, field_type_description, field_type_name, field_order_number, pattern, sample_value, document_type_id) VALUES (18,now(),now(),'STRING','Invoice Total','Invoice Total', 3, '!Sub;Total;[0-9]{1,3},?[0-9]{1,3}\\S[0-9]{2}','wx.yz',2);
INSERT INTO field_type(id, creation_date, last_modified, field_data_type, field_type_description, field_type_name, field_order_number, pattern, sample_value, document_type_id) VALUES (19,now(),now(),'STRING','State','State', 4, 'CA|NY|AZ|NV|NJ','CA|NY|AZ|NV|NJ',2);
INSERT INTO field_type(id, creation_date, last_modified, field_data_type, field_type_description, field_type_name, field_order_number, pattern, sample_value, document_type_id) VALUES (20,now(),now(),'STRING','City','City', 5, 'Irvine|Paris|Sydney','Irvine|Paris|Sydney' ,2);
INSERT INTO field_type(id, creation_date, last_modified, field_data_type, field_type_description, field_type_name, field_order_number, pattern, sample_value, document_type_id) VALUES (21,now(),now(),'STRING','Invoice Date','Invoice Date', 1, 'Invoice;Date:;[0-9]{2}/[0-9]{2}/[0-9]{2,4}','dd/mm/yyyy',3);
INSERT INTO field_type(id, creation_date, last_modified, field_data_type, field_type_description, field_type_name, field_order_number, pattern, sample_value, document_type_id) VALUES (22,now(),now(),'STRING','Part Number','Part Number', 2, '998[0-9]{9}','998xxxxxxxxx',3);
INSERT INTO field_type(id, creation_date, last_modified, field_data_type, field_type_description, field_type_name, field_order_number, pattern, sample_value, document_type_id) VALUES (23,now(),now(),'STRING','Invoice Total','Invoice Total', 3, '!Sub;Total;[0-9]{1,3},?[0-9]{1,3}\\S[0-9]{2}','wx.yz',3);
INSERT INTO field_type(id, creation_date, last_modified, field_data_type, field_type_description, field_type_name, field_order_number, pattern, sample_value, document_type_id) VALUES (24,now(),now(),'STRING','State','State', 4, 'CA|NY|AZ|NV|NJ','CA|NY|AZ|NV|NJ',3);
INSERT INTO field_type(id, creation_date, last_modified, field_data_type, field_type_description, field_type_name, field_order_number, pattern, sample_value, document_type_id) VALUES (25,now(),now(),'STRING','City','City', 5, 'Irvine|Paris|Sydney','Irvine|Paris|Sydney' ,3);
INSERT INTO field_type(id, creation_date, last_modified, field_data_type, field_type_description, field_type_name, field_order_number, pattern, sample_value, document_type_id) VALUES (26,now(),now(),'STRING','Invoice Date','Invoice Date', 1, 'Invoice;Date:;[0-9]{2}/[0-9]{2}/[0-9]{2,4}','dd/mm/yyyy',4);
INSERT INTO field_type(id, creation_date, last_modified, field_data_type, field_type_description, field_type_name, field_order_number, pattern, sample_value, document_type_id) VALUES (27,now(),now(),'STRING','Part Number','Part Number', 2, '998[0-9]{9}','998xxxxxxxxx',4);
INSERT INTO field_type(id, creation_date, last_modified, field_data_type, field_type_description, field_type_name, field_order_number, pattern, sample_value, document_type_id) VALUES (28,now(),now(),'STRING','Invoice Total','Invoice Total', 3, '!Sub;Total;[0-9]{1,3},?[0-9]{1,3}\\S[0-9]{2}','wx.yz',4);
INSERT INTO field_type(id, creation_date, last_modified, field_data_type, field_type_description, field_type_name, field_order_number, pattern, sample_value, document_type_id) VALUES (29,now(),now(),'STRING','State','State', 4, 'CA|NY|AZ|NV|NJ','CA|NY|AZ|NV|NJ',4);
INSERT INTO field_type(id, creation_date, last_modified, field_data_type, field_type_description, field_type_name, field_order_number, pattern, sample_value, document_type_id) VALUES (30,now(),now(),'STRING','City','City', 5, 'Irvine|Paris|Sydney','Irvine|Paris|Sydney' ,4);
INSERT INTO field_type(id, creation_date, last_modified, field_data_type, field_type_description, field_type_name, field_order_number, pattern, sample_value, document_type_id) VALUES (31,now(),now(),'STRING','Invoice Date','Invoice Date', 1, 'Invoice;Date:;[0-9]{2}/[0-9]{2}/[0-9]{2,4}','dd/mm/yyyy',9);
INSERT INTO field_type(id, creation_date, last_modified, field_data_type, field_type_description, field_type_name, field_order_number, pattern, sample_value, document_type_id) VALUES (32,now(),now(),'STRING','Part Number','Part Number', 2, '998[0-9]{9}','998xxxxxxxxx',9);
INSERT INTO field_type(id, creation_date, last_modified, field_data_type, field_type_description, field_type_name, field_order_number, pattern, sample_value, document_type_id) VALUES (33,now(),now(),'STRING','Invoice Total','Invoice Total', 3, '!Sub;Total;[0-9]{1,3},?[0-9]{1,3}\\S[0-9]{2}','wx.yz',9);
INSERT INTO field_type(id, creation_date, last_modified, field_data_type, field_type_description, field_type_name, field_order_number, pattern, sample_value, document_type_id) VALUES (34,now(),now(),'STRING','State','State', 4, 'CA|NY|AZ|NV|NJ','CA|NY|AZ|NV|NJ',9);
INSERT INTO field_type(id, creation_date, last_modified, field_data_type, field_type_description, field_type_name, field_order_number, pattern, sample_value, document_type_id) VALUES (35,now(),now(),'STRING','City','City', 5, 'Irvine|Paris|Sydney','Irvine|Paris|Sydney' ,9);
INSERT INTO field_type(id, creation_date, last_modified, field_data_type, field_type_description, field_type_name, field_order_number, pattern, sample_value, document_type_id) VALUES (36,now(),now(),'STRING','Invoice Date','Invoice Date', 1, 'Invoice;Date:;[0-9]{2}/[0-9]{2}/[0-9]{2,4}','dd/mm/yyyy',10);
INSERT INTO field_type(id, creation_date, last_modified, field_data_type, field_type_description, field_type_name, field_order_number, pattern, sample_value, document_type_id) VALUES (37,now(),now(),'STRING','Part Number','Part Number', 2, '998[0-9]{9}','998xxxxxxxxx',10);
INSERT INTO field_type(id, creation_date, last_modified, field_data_type, field_type_description, field_type_name, field_order_number, pattern, sample_value, document_type_id) VALUES (38,now(),now(),'STRING','Invoice Total','Invoice Total', 3, '!Sub;Total;[0-9]{1,3},?[0-9]{1,3}\\S[0-9]{2}','wx.yz',10);
INSERT INTO field_type(id, creation_date, last_modified, field_data_type, field_type_description, field_type_name, field_order_number, pattern, sample_value, document_type_id) VALUES (39,now(),now(),'STRING','State','State', 4, 'CA|NY|AZ|NV|NJ','CA|NY|AZ|NV|NJ',10);
INSERT INTO field_type(id, creation_date, last_modified, field_data_type, field_type_description, field_type_name, field_order_number, pattern, sample_value, document_type_id) VALUES (40,now(),now(),'STRING','City','City', 5, 'Irvine|Paris|Sydney','Irvine|Paris|Sydney' ,10);
INSERT INTO field_type(id, creation_date, last_modified, field_data_type, field_type_description, field_type_name, field_order_number, pattern, sample_value, document_type_id) VALUES (41,now(),now(),'STRING','Invoice Date','Invoice Date', 1, 'Invoice;Date:;[0-9]{2}/[0-9]{2}/[0-9]{2,4}','dd/mm/yyyy',11);
INSERT INTO field_type(id, creation_date, last_modified, field_data_type, field_type_description, field_type_name, field_order_number, pattern, sample_value, document_type_id) VALUES (42,now(),now(),'STRING','Part Number','Part Number', 2, '998[0-9]{9}','998xxxxxxxxx',11);
INSERT INTO field_type(id, creation_date, last_modified, field_data_type, field_type_description, field_type_name, field_order_number, pattern, sample_value, document_type_id) VALUES (43,now(),now(),'STRING','Invoice Total','Invoice Total', 3, '!Sub;Total;[0-9]{1,3},?[0-9]{1,3}\\S[0-9]{2}','wx.yz',11);
INSERT INTO field_type(id, creation_date, last_modified, field_data_type, field_type_description, field_type_name, field_order_number, pattern, sample_value, document_type_id) VALUES (44,now(),now(),'STRING','State','State', 4, 'CA|NY|AZ|NV|NJ','CA|NY|AZ|NV|NJ',11);
INSERT INTO field_type(id, creation_date, last_modified, field_data_type, field_type_description, field_type_name, field_order_number, pattern, sample_value, document_type_id) VALUES (45,now(),now(),'STRING','City','City', 5, 'Irvine|Paris|Sydney','Irvine|Paris|Sydney' ,11);
INSERT INTO field_type(id, creation_date, last_modified, field_data_type, field_type_description, field_type_name, field_order_number, pattern, sample_value, document_type_id) VALUES (46,now(),now(),'STRING','Invoice Date','Invoice Date', 1, 'Invoice;Date:;[0-9]{2}/[0-9]{2}/[0-9]{2,4}','dd/mm/yyyy',12);
INSERT INTO field_type(id, creation_date, last_modified, field_data_type, field_type_description, field_type_name, field_order_number, pattern, sample_value, document_type_id) VALUES (47,now(),now(),'STRING','Part Number','Part Number', 2, '998[0-9]{9}','998xxxxxxxxx',12);
INSERT INTO field_type(id, creation_date, last_modified, field_data_type, field_type_description, field_type_name, field_order_number, pattern, sample_value, document_type_id) VALUES (48,now(),now(),'STRING','Invoice Total','Invoice Total', 3, '!Sub;Total;[0-9]{1,3},?[0-9]{1,3}\\S[0-9]{2}','wx.yz',12);
INSERT INTO field_type(id, creation_date, last_modified, field_data_type, field_type_description, field_type_name, field_order_number, pattern, sample_value, document_type_id) VALUES (49,now(),now(),'STRING','State','State', 4, 'CA|NY|AZ|NV|NJ','CA|NY|AZ|NV|NJ',12);
INSERT INTO field_type(id, creation_date, last_modified, field_data_type, field_type_description, field_type_name, field_order_number, pattern, sample_value, document_type_id) VALUES (50,now(),now(),'STRING','City','City', 5, 'Irvine|Paris|Sydney','Irvine|Paris|Sydney' ,12);
INSERT INTO field_type(id, creation_date, last_modified, field_data_type, field_type_description, field_type_name, field_order_number, pattern, sample_value, document_type_id) VALUES (51,now(),now(),'STRING','Field1','Field1', 1, '[0-9]','x',13);
INSERT INTO field_type(id, creation_date, last_modified, field_data_type, field_type_description, field_type_name, field_order_number, pattern, sample_value, document_type_id) VALUES (52,now(),now(),'STRING','Field2','Field2', 2, '[0-9]','x',13);
INSERT INTO field_type(id, creation_date, last_modified, field_data_type, field_type_description, field_type_name, field_order_number, pattern, sample_value, document_type_id) VALUES (53,now(),now(),'STRING','Field3','Field3', 3, '[0-9]','x',13);
INSERT INTO field_type(id, creation_date, last_modified, field_data_type, field_type_description, field_type_name, field_order_number, pattern, sample_value, document_type_id) VALUES (54,now(),now(),'STRING','Field4','Field4', 4, '[0-9]','x',13);
INSERT INTO field_type(id, creation_date, last_modified, field_data_type, field_type_description, field_type_name, field_order_number, pattern, sample_value, document_type_id) VALUES (55,now(),now(),'STRING','Field5','Field5', 5, '[0-9]','x',13);
INSERT INTO field_type(id, creation_date, last_modified, field_data_type, field_type_description, field_type_name, field_order_number, pattern, sample_value, document_type_id) VALUES (56,now(),now(),'STRING','Field1','Field1', 1, '[0-9]','x',14);
INSERT INTO field_type(id, creation_date, last_modified, field_data_type, field_type_description, field_type_name, field_order_number, pattern, sample_value, document_type_id) VALUES (57,now(),now(),'STRING','Invoice Date','Invoice Date', 1, 'Invoice;Date:;[0-9]{2}/[0-9]{2}/[0-9]{2,4}', 'dd/mm/yyyy', 17);
INSERT INTO field_type(id, creation_date, last_modified, field_data_type, field_type_description, field_type_name, field_order_number, pattern, sample_value, document_type_id) VALUES (58,now(),now(),'STRING','Part Number','Part Number', 2, '998[0-9]{9}', '998xxxxxxxxx', 17);
INSERT INTO field_type(id, creation_date, last_modified, field_data_type, field_type_description, field_type_name, field_order_number, pattern, sample_value, document_type_id) VALUES (59,now(),now(),'STRING','Invoice Total','Invoice Total', 3, '!Sub;Total;[0-9]{1,3},?[0-9]{1,3}\\S[0-9]{2}','wx.yz',17);
INSERT INTO field_type(id, creation_date, last_modified, field_data_type, field_type_description, field_type_name, field_order_number, pattern, sample_value, document_type_id) VALUES (60,now(),now(),'STRING','State','State', 4, 'CA|NY|AZ|NV|NJ','CA|NY|AZ|NV|NJ',17);
INSERT INTO field_type(id, creation_date, last_modified, field_data_type, field_type_description, field_type_name, field_order_number, pattern, sample_value, document_type_id) VALUES (61,now(),now(),'STRING','City','City', 5, 'Irvine|Paris|Sydney','Irvine|Paris|Sydney' ,17);
INSERT INTO field_type(id, creation_date, last_modified, field_data_type, field_type_description, field_type_name, field_order_number, pattern, sample_value, document_type_id) VALUES (62,now(),now(),'STRING','Invoice Date','Invoice Date', 1, 'Invoice;Date:;[0-9]{2}/[0-9]{2}/[0-9]{2,4}', 'dd/mm/yyyy', 18);
INSERT INTO field_type(id, creation_date, last_modified, field_data_type, field_type_description, field_type_name, field_order_number, pattern, sample_value, document_type_id) VALUES (63,now(),now(),'STRING','Part Number','Part Number', 2, '998[0-9]{9}', '998xxxxxxxxx', 18);
INSERT INTO field_type(id, creation_date, last_modified, field_data_type, field_type_description, field_type_name, field_order_number, pattern, sample_value, document_type_id) VALUES (64,now(),now(),'STRING','Invoice Total','Invoice Total', 3, '!Sub;Total;[0-9]{1,3},?[0-9]{1,3}\\S[0-9]{2}','wx.yz',18);
INSERT INTO field_type(id, creation_date, last_modified, field_data_type, field_type_description, field_type_name, field_order_number, pattern, sample_value, document_type_id) VALUES (65,now(),now(),'STRING','State','State', 4, 'CA|NY|AZ|NV|NJ','CA|NY|AZ|NV|NJ',18);
INSERT INTO field_type(id, creation_date, last_modified, field_data_type, field_type_description, field_type_name, field_order_number, pattern, sample_value, document_type_id) VALUES (66,now(),now(),'STRING','City','City', 5, 'Irvine|Paris|Sydney','Irvine|Paris|Sydney' ,18);
INSERT INTO field_type(id, creation_date, last_modified, field_data_type, field_type_description, field_type_name, field_order_number, pattern, sample_value, document_type_id) VALUES (67,now(),now(),'STRING','Invoice Date','Invoice Date', 1, 'Invoice;Date:;[0-9]{2}/[0-9]{2}/[0-9]{2,4}', 'dd/mm/yyyy', 19);
INSERT INTO field_type(id, creation_date, last_modified, field_data_type, field_type_description, field_type_name, field_order_number, pattern, sample_value, document_type_id) VALUES (68,now(),now(),'STRING','Part Number','Part Number', 2, '998[0-9]{9}', '998xxxxxxxxx', 19);
INSERT INTO field_type(id, creation_date, last_modified, field_data_type, field_type_description, field_type_name, field_order_number, pattern, sample_value, document_type_id) VALUES (69,now(),now(),'STRING','Invoice Total','Invoice Total', 3, '!Sub;Total;[0-9]{1,3},?[0-9]{1,3}\\S[0-9]{2}','wx.yz',19);
INSERT INTO field_type(id, creation_date, last_modified, field_data_type, field_type_description, field_type_name, field_order_number, pattern, sample_value, document_type_id) VALUES (70,now(),now(),'STRING','State','State', 4, 'CA|NY|AZ|NV|NJ','CA|NY|AZ|NV|NJ',19);
INSERT INTO field_type(id, creation_date, last_modified, field_data_type, field_type_description, field_type_name, field_order_number, pattern, sample_value, document_type_id) VALUES (71,now(),now(),'STRING','City','City', 5, 'Irvine|Paris|Sydney','Irvine|Paris|Sydney' ,19);

 
/*Page Type*/
INSERT INTO page_type(id, creation_date, last_modified, page_type_description, page_type_name, document_type_id) VALUES (1,now(),now(),'Application-Checklist_First_Page page','Application-Checklist_First_Page',2);
INSERT INTO page_type(id, creation_date, last_modified, page_type_description, page_type_name, document_type_id) VALUES (2,now(),now(),'Application-Checklist_Middle_Page page','Application-Checklist_Middle_Page',2);
INSERT INTO page_type(id, creation_date, last_modified, page_type_description, page_type_name, document_type_id) VALUES (3,now(),now(),'Application-Checklist_Last_Page page','Application-Checklist_Last_Page',2);
INSERT INTO page_type(id, creation_date, last_modified, page_type_description, page_type_name, document_type_id) VALUES (4,now(),now(),'Workers-Comp-02_First_Page page','Workers-Comp-02_First_Page',3);
INSERT INTO page_type(id, creation_date, last_modified, page_type_description, page_type_name, document_type_id) VALUES (5,now(),now(),'Workers-Comp-02_Middle_Page page','Workers-Comp-02_Middle_Page',3);
INSERT INTO page_type(id, creation_date, last_modified, page_type_description, page_type_name, document_type_id) VALUES (6,now(),now(),'Workers-Comp-02_Last_Page page','Workers-Comp-02_Last_Page',3);
INSERT INTO page_type(id, creation_date, last_modified, page_type_description, page_type_name, document_type_id) VALUES (7,now(),now(),'US-Invoice-Data_First_Page page','US-Invoice-Data_First_Page',4);
INSERT INTO page_type(id, creation_date, last_modified, page_type_description, page_type_name, document_type_id) VALUES (8,now(),now(),'US-Invoice-Data_Middle_Page page','US-Invoice-Data_Middle_Page',4);
INSERT INTO page_type(id, creation_date, last_modified, page_type_description, page_type_name, document_type_id) VALUES (9,now(),now(),'US-Invoice-Data_Last_Page page','US-Invoice-Data_Last_Page',4);
INSERT INTO page_type(id, creation_date, last_modified, page_type_description, page_type_name, document_type_id) VALUES (10,now(),now(),'Application-Checklist_First_Page page','Application-Checklist_First_Page',6);
INSERT INTO page_type(id, creation_date, last_modified, page_type_description, page_type_name, document_type_id) VALUES (11,now(),now(),'Application-Checklist_Middle_Page page','Application-Checklist_Middle_Page',6);
INSERT INTO page_type(id, creation_date, last_modified, page_type_description, page_type_name, document_type_id) VALUES (12,now(),now(),'Application-Checklist_Last_Page page','Application-Checklist_Last_Page',6);
INSERT INTO page_type(id, creation_date, last_modified, page_type_description, page_type_name, document_type_id) VALUES (13,now(),now(),'Workers-Comp-02_First_Page page','Workers-Comp-02_First_Page',7);
INSERT INTO page_type(id, creation_date, last_modified, page_type_description, page_type_name, document_type_id) VALUES (14,now(),now(),'Workers-Comp-02_Middle_Page page','Workers-Comp-02_Middle_Page',7);
INSERT INTO page_type(id, creation_date, last_modified, page_type_description, page_type_name, document_type_id) VALUES (15,now(),now(),'Workers-Comp-02_Last_Page page','Workers-Comp-02_Last_Page',7);
INSERT INTO page_type(id, creation_date, last_modified, page_type_description, page_type_name, document_type_id) VALUES (16,now(),now(),'US-Invoice-Data_First_Page page','US-Invoice-Data_First_Page',8);
INSERT INTO page_type(id, creation_date, last_modified, page_type_description, page_type_name, document_type_id) VALUES (17,now(),now(),'US-Invoice-Data_Middle_Page page','US-Invoice-Data_Middle_Page',8);
INSERT INTO page_type(id, creation_date, last_modified, page_type_description, page_type_name, document_type_id) VALUES (18,now(),now(),'US-Invoice-Data_Last_Page page','US-Invoice-Data_Last_Page',8);
INSERT INTO page_type(id, creation_date, last_modified, page_type_description, page_type_name, document_type_id) VALUES (19,now(),now(),'Invoice-Table_First_Page page','Invoice-Table_First_Page',9);
INSERT INTO page_type(id, creation_date, last_modified, page_type_description, page_type_name, document_type_id) VALUES (20,now(),now(),'Invoice-Table_Middle_Page page','Invoice-Table_Middle_Page',9);
INSERT INTO page_type(id, creation_date, last_modified, page_type_description, page_type_name, document_type_id) VALUES (21,now(),now(),'Invoice-Table_Last_Page page','Invoice-Table_Last_Page',9);
INSERT INTO page_type(id, creation_date, last_modified, page_type_description, page_type_name, document_type_id) VALUES (22,now(),now(),'Invoice-Table_First_Page page','Invoice-Table_First_Page',10);
INSERT INTO page_type(id, creation_date, last_modified, page_type_description, page_type_name, document_type_id) VALUES (23,now(),now(),'Invoice-Table_Middle_Page page','Invoice-Table_Middle_Page',10);
INSERT INTO page_type(id, creation_date, last_modified, page_type_description, page_type_name, document_type_id) VALUES (24,now(),now(),'Invoice-Table_Last_Page page','Invoice-Table_Last_Page',10);
INSERT INTO page_type(id, creation_date, last_modified, page_type_description, page_type_name, document_type_id) VALUES (25,now(),now(),'US Invoice_First_Page page','US Invoice_First_Page',11);
INSERT INTO page_type(id, creation_date, last_modified, page_type_description, page_type_name, document_type_id) VALUES (26,now(),now(),'US Invoice_Middle_Page page','US Invoice_Middle_Page',11);
INSERT INTO page_type(id, creation_date, last_modified, page_type_description, page_type_name, document_type_id) VALUES (27,now(),now(),'US Invoice_Last_Page page','US Invoice_Last_Page',11);
INSERT INTO page_type(id, creation_date, last_modified, page_type_description, page_type_name, document_type_id) VALUES (28,now(),now(),'US Invoice_First_Page page','US Invoice_First_Page',12);
INSERT INTO page_type(id, creation_date, last_modified, page_type_description, page_type_name, document_type_id) VALUES (29,now(),now(),'US Invoice_Middle_Page page','US Invoice_Middle_Page',12);
INSERT INTO page_type(id, creation_date, last_modified, page_type_description, page_type_name, document_type_id) VALUES (30,now(),now(),'US Invoice_Last_Page page','US Invoice_Last_Page',12);
INSERT INTO page_type(id, creation_date, last_modified, page_type_description, page_type_name, document_type_id) VALUES (31,now(),now(),'Voting_Pharmacy_First_Page page','Voting_Pharmacy_First_Page',13);
INSERT INTO page_type(id, creation_date, last_modified, page_type_description, page_type_name, document_type_id) VALUES (32,now(),now(),'Voting_Pharmacy_Middle_Page page','Voting_Pharmacy_Middle_Page',13);
INSERT INTO page_type(id, creation_date, last_modified, page_type_description, page_type_name, document_type_id) VALUES (33,now(),now(),'Voting_Pharmacy_Last_Page page','Voting_Pharmacy_Last_Page',13);
INSERT INTO page_type(id, creation_date, last_modified, page_type_description, page_type_name, document_type_id) VALUES (34,now(),now(),'Voting_Machine_First_Page page','Voting_Machine_First_Page',14);
INSERT INTO page_type(id, creation_date, last_modified, page_type_description, page_type_name, document_type_id) VALUES (35,now(),now(),'Voting_Machine_Middle_Page page','Voting_Machine_Middle_Page',14);
INSERT INTO page_type(id, creation_date, last_modified, page_type_description, page_type_name, document_type_id) VALUES (36,now(),now(),'Voting_Machine_Last_Page page','Voting_Machine_Last_Page',14);
INSERT INTO page_type(id, creation_date, last_modified, page_type_description, page_type_name, document_type_id) VALUES (37,now(),now(),'Searchable Document_First_Page page','Searchable Document_First_Page',15);
INSERT INTO page_type(id, creation_date, last_modified, page_type_description, page_type_name, document_type_id) VALUES (38,now(),now(),'Searchable Document_Middle_Page page','Searchable Document_Middle_Page',15);
INSERT INTO page_type(id, creation_date, last_modified, page_type_description, page_type_name, document_type_id) VALUES (39,now(),now(),'Searchable Document_Last_Page page','Searchable Document_Last_Page',15);
INSERT INTO page_type(id, creation_date, last_modified, page_type_description, page_type_name, document_type_id) VALUES (40,now(),now(),'Application-Checklist_First_Page page','Application-Checklist_First_Page',17);
INSERT INTO page_type(id, creation_date, last_modified, page_type_description, page_type_name, document_type_id) VALUES (41,now(),now(),'Application-Checklist_Middle_Page page','Application-Checklist_Middle_Page',17);
INSERT INTO page_type(id, creation_date, last_modified, page_type_description, page_type_name, document_type_id) VALUES (42,now(),now(),'Application-Checklist_Last_Page page','Application-Checklist_Last_Page',17);
INSERT INTO page_type(id, creation_date, last_modified, page_type_description, page_type_name, document_type_id) VALUES (43,now(),now(),'Workers-Comp-02_First_Page page','Workers-Comp-02_First_Page',18);
INSERT INTO page_type(id, creation_date, last_modified, page_type_description, page_type_name, document_type_id) VALUES (44,now(),now(),'Workers-Comp-02_Middle_Page page','Workers-Comp-02_Middle_Page',18);
INSERT INTO page_type(id, creation_date, last_modified, page_type_description, page_type_name, document_type_id) VALUES (45,now(),now(),'Workers-Comp-02_Last_Page page','Workers-Comp-02_Last_Page',18);
INSERT INTO page_type(id, creation_date, last_modified, page_type_description, page_type_name, document_type_id) VALUES (46,now(),now(),'US-Invoice-Data_First_Page page','US-Invoice-Data_First_Page',19);
INSERT INTO page_type(id, creation_date, last_modified, page_type_description, page_type_name, document_type_id) VALUES (47,now(),now(),'US-Invoice-Data_Middle_Page page','US-Invoice-Data_Middle_Page',19);
INSERT INTO page_type(id, creation_date, last_modified, page_type_description, page_type_name, document_type_id) VALUES (48,now(),now(),'US-Invoice-Data_Last_Page page','US-Invoice-Data_Last_Page',19);



/*Key Value Extraction*/
INSERT INTO kv_extraction(id,creation_date,last_modified,key_pattern,location,value_pattern,field_type_id) VALUES (1,sysdate(),now(),'completed','TOP_LEFT','Please',1);
INSERT INTO kv_extraction(id,creation_date,last_modified,key_pattern,location,value_pattern,field_type_id) VALUES (2,now(),now(),'checklist','TOP_RIGHT','prior',2);
INSERT INTO kv_extraction(id,creation_date,last_modified,key_pattern,location,value_pattern,field_type_id) VALUES (3,now(),now(),'the','TOP','help',3);
INSERT INTO kv_extraction(id,creation_date,last_modified,key_pattern,location,value_pattern,field_type_id) VALUES (4,now(),now(),'checklist','LEFT','completed',4);
INSERT INTO kv_extraction(id,creation_date,last_modified,key_pattern,location,value_pattern,field_type_id) VALUES (5,now(),now(),'with','RIGHT','beneficial',5);
INSERT INTO kv_extraction(id,creation_date,last_modified,key_pattern,location,value_pattern,field_type_id) VALUES (6,now(),now(),'deposit','BOTTOM_RIGHT','states',6);
INSERT INTO kv_extraction(id,creation_date,last_modified,key_pattern,location,value_pattern,field_type_id) VALUES (7,now(),now(),'association','BOTTOM','set',7);
INSERT INTO kv_extraction(id,creation_date,last_modified,key_pattern,location,value_pattern,field_type_id) VALUES (8,now(),now(),'order','BOTTOM_LEFT','must',8);
INSERT INTO kv_extraction(id,creation_date,last_modified,key_pattern,location,value_pattern,field_type_id) VALUES (9,now(),now(),'association','BOTTOM','set',9);
INSERT INTO kv_extraction(id,creation_date,last_modified,key_pattern,location,value_pattern,field_type_id) VALUES (10,now(),now(),'order','BOTTOM_LEFT','must',10);
INSERT INTO kv_extraction(id,creation_date,last_modified,key_pattern,location,value_pattern,field_type_id) VALUES (11,now(),now(),'Date','RIGHT','[0-9]{2}/[0-9]{2}/[0-9]{2,4}',11);
INSERT INTO kv_extraction(id,creation_date,last_modified,key_pattern,location,value_pattern,field_type_id) VALUES (12,now(),now(),'Part','BOTTOM','998[0-9]{9}',12);
INSERT INTO kv_extraction(id,creation_date,last_modified,key_pattern,location,value_pattern,field_type_id) VALUES (13,now(),now(),'Total','RIGHT','[0-9]{1,3},?[0-9]{1,3}\\S[0-9]{2}',13);
INSERT INTO kv_extraction(id,creation_date,last_modified,key_pattern,location,value_pattern,field_type_id) VALUES (14,now(),now(),'Hills','BOTTOM_LEFT','CA|NY|AZ|NV|NJ',14);
INSERT INTO kv_extraction(id,creation_date,last_modified,key_pattern,location,value_pattern,field_type_id) VALUES (15,now(),now(),'Belverly','BOTTOM','Irvine',15);
INSERT INTO kv_extraction(id,creation_date,last_modified,key_pattern,location,value_pattern,field_type_id) VALUES (16,now(),now(),'completed','TOP_LEFT','Please',16);
INSERT INTO kv_extraction(id,creation_date,last_modified,key_pattern,location,value_pattern,field_type_id) VALUES (17,now(),now(),'checklist','TOP_RIGHT','prior',17);
INSERT INTO kv_extraction(id,creation_date,last_modified,key_pattern,location,value_pattern,field_type_id) VALUES (18,now(),now(),'the','TOP','help',18);
INSERT INTO kv_extraction(id,creation_date,last_modified,key_pattern,location,value_pattern,field_type_id) VALUES (19,now(),now(),'checklist','LEFT','completed',19);
INSERT INTO kv_extraction(id,creation_date,last_modified,key_pattern,location,value_pattern,field_type_id) VALUES (20,now(),now(),'with','RIGHT','beneficial',20);
INSERT INTO kv_extraction(id,creation_date,last_modified,key_pattern,location,value_pattern,field_type_id) VALUES (21,now(),now(),'deposit','BOTTOM_RIGHT','states',21);
INSERT INTO kv_extraction(id,creation_date,last_modified,key_pattern,location,value_pattern,field_type_id) VALUES (22,now(),now(),'association','BOTTOM','set',22);
INSERT INTO kv_extraction(id,creation_date,last_modified,key_pattern,location,value_pattern,field_type_id) VALUES (23,now(),now(),'order','BOTTOM_LEFT','must',23);
INSERT INTO kv_extraction(id,creation_date,last_modified,key_pattern,location,value_pattern,field_type_id) VALUES (24,now(),now(),'association','BOTTOM','set',24);
INSERT INTO kv_extraction(id,creation_date,last_modified,key_pattern,location,value_pattern,field_type_id) VALUES (25,now(),now(),'order','BOTTOM_LEFT','must',25);
INSERT INTO kv_extraction(id,creation_date,last_modified,key_pattern,location,value_pattern,field_type_id) VALUES (26,now(),now(),'Date','RIGHT','[0-9]{2}/[0-9]{2}/[0-9]{2,4}',26);
INSERT INTO kv_extraction(id,creation_date,last_modified,key_pattern,location,value_pattern,field_type_id) VALUES (27,now(),now(),'Pa*r*t','BOTTOM','998[0-9]{9}',27);
INSERT INTO kv_extraction(id,creation_date,last_modified,key_pattern,location,value_pattern,field_type_id) VALUES (28,now(),now(),'Total','RIGHT','[0-9]{1,3},?[0-9]{1,3}\\S[0-9]{2}',28);
INSERT INTO kv_extraction(id,creation_date,last_modified,key_pattern,location,value_pattern,field_type_id) VALUES (29,now(),now(),'Hills','BOTTOM_LEFT','CA|NY|AZ|NV|NJ',29);
INSERT INTO kv_extraction(id,creation_date,last_modified,key_pattern,location,value_pattern,field_type_id) VALUES (30,now(),now(),'Belverly','BOTTOM','Irvine',30);
INSERT INTO kv_extraction(id,creation_date,last_modified,key_pattern,location,value_pattern,field_type_id) VALUES (31,now(),now(),'Date','RIGHT','[0-9]{2}/[0-9]{2}/[0-9]{2,4}',31);
INSERT INTO kv_extraction(id,creation_date,last_modified,key_pattern,location,value_pattern,field_type_id) VALUES (32,now(),now(),'Pa*r*t','BOTTOM','998[0-9]{9}',32);
INSERT INTO kv_extraction(id,creation_date,last_modified,key_pattern,location,value_pattern,field_type_id) VALUES (33,now(),now(),'Total','RIGHT','[0-9]{1,3},?[0-9]{1,3}\\S[0-9]{2}',33);
INSERT INTO kv_extraction(id,creation_date,last_modified,key_pattern,location,value_pattern,field_type_id) VALUES (34,now(),now(),'Hills','BOTTOM_LEFT','CA|NY|AZ|NV|NJ',34);
INSERT INTO kv_extraction(id,creation_date,last_modified,key_pattern,location,value_pattern,field_type_id) VALUES (35,now(),now(),'Belverly','BOTTOM','Irvine',35);
INSERT INTO kv_extraction(id,creation_date,last_modified,key_pattern,location,value_pattern,field_type_id) VALUES (36,now(),now(),'Date','RIGHT','[0-9]{2}/[0-9]{2}/[0-9]{2,4}',36);
INSERT INTO kv_extraction(id,creation_date,last_modified,key_pattern,location,value_pattern,field_type_id) VALUES (37,now(),now(),'Pa*r*t','BOTTOM','998[0-9]{9}',37);
INSERT INTO kv_extraction(id,creation_date,last_modified,key_pattern,location,value_pattern,field_type_id) VALUES (38,now(),now(),'Total','RIGHT','[0-9]{1,3},?[0-9]{1,3}\\S[0-9]{2}',38);
INSERT INTO kv_extraction(id,creation_date,last_modified,key_pattern,location,value_pattern,field_type_id) VALUES (39,now(),now(),'Hills','BOTTOM_LEFT','CA|NY|AZ|NV|NJ',39);
INSERT INTO kv_extraction(id,creation_date,last_modified,key_pattern,location,value_pattern,field_type_id) VALUES (40,now(),now(),'Belverly','BOTTOM','Irvine',40);
INSERT INTO kv_extraction(id,creation_date,last_modified,key_pattern,location,value_pattern,field_type_id) VALUES (41,now(),now(),'Date','RIGHT','[0-9]{2}/[0-9]{2}/[0-9]{2,4}',41);
INSERT INTO kv_extraction(id,creation_date,last_modified,key_pattern,location,value_pattern,field_type_id) VALUES (42,now(),now(),'Pa*r*t','BOTTOM','998[0-9]{9}',42);
INSERT INTO kv_extraction(id,creation_date,last_modified,key_pattern,location,value_pattern,field_type_id) VALUES (43,now(),now(),'Total','RIGHT','[0-9]{1,3},?[0-9]{1,3}\\S[0-9]{2}',43);
INSERT INTO kv_extraction(id,creation_date,last_modified,key_pattern,location,value_pattern,field_type_id) VALUES (44,now(),now(),'Hills','BOTTOM_LEFT','CA|NY|AZ|NV|NJ',44);
INSERT INTO kv_extraction(id,creation_date,last_modified,key_pattern,location,value_pattern,field_type_id) VALUES (45,now(),now(),'Belverly','BOTTOM','Irvine',45);
INSERT INTO kv_extraction(id,creation_date,last_modified,key_pattern,location,value_pattern,field_type_id) VALUES (46,now(),now(),'Date','RIGHT','[0-9]{2}/[0-9]{2}/[0-9]{2,4}',46);
INSERT INTO kv_extraction(id,creation_date,last_modified,key_pattern,location,value_pattern,field_type_id) VALUES (47,now(),now(),'Pa*r*t','BOTTOM','998[0-9]{9}',47);
INSERT INTO kv_extraction(id,creation_date,last_modified,key_pattern,location,value_pattern,field_type_id) VALUES (48,now(),now(),'Total','RIGHT','[0-9]{1,3},?[0-9]{1,3}\\S[0-9]{2}',48);
INSERT INTO kv_extraction(id,creation_date,last_modified,key_pattern,location,value_pattern,field_type_id) VALUES (49,now(),now(),'Hills','BOTTOM_LEFT','CA|NY|AZ|NV|NJ',49);
INSERT INTO kv_extraction(id,creation_date,last_modified,key_pattern,location,value_pattern,field_type_id) VALUES (50,now(),now(),'Belverly','BOTTOM','Irvine',50);
INSERT INTO kv_extraction(id,creation_date,last_modified,key_pattern,location,value_pattern,field_type_id) VALUES (51,now(),now(),'998[0-9]{9}','BOTTOM','998[0-9]{8}',27);

/*Identifiers Update*/
update batch_class set identifier = concat('BC',hex(id));
update document_type set identifier = concat('DT',hex(id));
update page_type set identifier = concat('PT',hex(id));
update field_type set identifier = concat('FT',hex(id));

/*Regular Expression inserts*/
INSERT INTO regex_validation(id, creation_date, last_modified, pattern, field_type_id) VALUES (1, now(), now(),'[0-9]{2}/[0-9]{2}/[0-9]{2,4}', 1);
INSERT INTO regex_validation(id, creation_date, last_modified, pattern, field_type_id) VALUES (2, now(), now(),'998[0-9]{9}', 2);
INSERT INTO regex_validation(id, creation_date, last_modified, pattern, field_type_id) VALUES (3, now(), now(),'[0-9]{1,3},?[0-9]{1,3}\\S[0-9]{2}', 3);
INSERT INTO regex_validation(id, creation_date, last_modified, pattern, field_type_id) VALUES (4, now(), now(),'CA|NY|AZ|NV|NJ', 4);
INSERT INTO regex_validation(id, creation_date, last_modified, pattern, field_type_id) VALUES (5, now(), now(),'Irvine|Paris|Sydney', 5);
INSERT INTO regex_validation(id, creation_date, last_modified, pattern, field_type_id) VALUES (6, now(), now(),'[0-9]{2}/[0-9]{2}/[0-9]{2,4}', 6);
INSERT INTO regex_validation(id, creation_date, last_modified, pattern, field_type_id) VALUES (7, now(), now(),'998[0-9]{9}', 7);
INSERT INTO regex_validation(id, creation_date, last_modified, pattern, field_type_id) VALUES (8, now(), now(),'[0-9]{1,3},?[0-9]{1,3}\\S[0-9]{2}', 8);
INSERT INTO regex_validation(id, creation_date, last_modified, pattern, field_type_id) VALUES (9, now(), now(),'CA|NY|AZ|NV|NJ', 9);
INSERT INTO regex_validation(id, creation_date, last_modified, pattern, field_type_id) VALUES (10, now(), now(),'Irvine|Paris|Sydney', 10);
INSERT INTO regex_validation(id, creation_date, last_modified, pattern, field_type_id) VALUES (11, now(), now(),'[0-9]{2}/[0-9]{2}/[0-9]{2,4}', 11);
INSERT INTO regex_validation(id, creation_date, last_modified, pattern, field_type_id) VALUES (12, now(), now(),'998[0-9]{9}', 12);
INSERT INTO regex_validation(id, creation_date, last_modified, pattern, field_type_id) VALUES (13, now(), now(),'[0-9]{1,3},?[0-9]{1,3}\\S[0-9]{2}', 13);
INSERT INTO regex_validation(id, creation_date, last_modified, pattern, field_type_id) VALUES (14, now(), now(),'CA|NY|AZ|NV|NJ', 14);
INSERT INTO regex_validation(id, creation_date, last_modified, pattern, field_type_id) VALUES (15, now(), now(),'Irvine|Paris|Sydney', 15);
INSERT INTO regex_validation(id, creation_date, last_modified, pattern, field_type_id) VALUES (16, now(), now(),'[0-9]{2}/[0-9]{2}/[0-9]{2,4}', 16);
INSERT INTO regex_validation(id, creation_date, last_modified, pattern, field_type_id) VALUES (17, now(), now(),'998[0-9]{9}', 17);
INSERT INTO regex_validation(id, creation_date, last_modified, pattern, field_type_id) VALUES (18, now(), now(),'[0-9]{1,3},?[0-9]{1,3}\\S[0-9]{2}', 18);
INSERT INTO regex_validation(id, creation_date, last_modified, pattern, field_type_id) VALUES (19, now(), now(),'CA|NY|AZ|NV|NJ', 19);
INSERT INTO regex_validation(id, creation_date, last_modified, pattern, field_type_id) VALUES (20, now(), now(),'Irvine|Paris|Sydney', 20);
INSERT INTO regex_validation(id, creation_date, last_modified, pattern, field_type_id) VALUES (21, now(), now(),'[0-9]{2}/[0-9]{2}/[0-9]{2,4}', 21);
INSERT INTO regex_validation(id, creation_date, last_modified, pattern, field_type_id) VALUES (22, now(), now(),'998[0-9]{9}', 22);
INSERT INTO regex_validation(id, creation_date, last_modified, pattern, field_type_id) VALUES (23, now(), now(),'[0-9]{1,3},?[0-9]{1,3}\\S[0-9]{2}', 23);
INSERT INTO regex_validation(id, creation_date, last_modified, pattern, field_type_id) VALUES (24, now(), now(),'CA|NY|AZ|NV|NJ', 24);
INSERT INTO regex_validation(id, creation_date, last_modified, pattern, field_type_id) VALUES (25, now(), now(),'Irvine|Paris|Sydney', 25);
INSERT INTO regex_validation(id, creation_date, last_modified, pattern, field_type_id) VALUES (26, now(), now(),'[0-9]{2}/[0-9]{2}/[0-9]{2,4}', 26);
INSERT INTO regex_validation(id, creation_date, last_modified, pattern, field_type_id) VALUES (27, now(), now(),'998[0-9]{9}', 27);
INSERT INTO regex_validation(id, creation_date, last_modified, pattern, field_type_id) VALUES (28, now(), now(),'[0-9]{1,3},?[0-9]{1,3}\\S[0-9]{2}', 28);
INSERT INTO regex_validation(id, creation_date, last_modified, pattern, field_type_id) VALUES (29, now(), now(),'CA|NY|AZ|NV|NJ', 29);
INSERT INTO regex_validation(id, creation_date, last_modified, pattern, field_type_id) VALUES (30, now(), now(),'Irvine|Paris|Sydney', 30);
INSERT INTO regex_validation(id, creation_date, last_modified, pattern, field_type_id) VALUES (31, now(), now(),'[0-9]{2}/[0-9]{2}/[0-9]{2,4}', 31);
INSERT INTO regex_validation(id, creation_date, last_modified, pattern, field_type_id) VALUES (32, now(), now(),'998[0-9]{9}', 32);
INSERT INTO regex_validation(id, creation_date, last_modified, pattern, field_type_id) VALUES (33, now(), now(),'[0-9]{1,3},?[0-9]{1,3}\\S[0-9]{2}', 33);
INSERT INTO regex_validation(id, creation_date, last_modified, pattern, field_type_id) VALUES (34, now(), now(),'CA|NY|AZ|NV|NJ', 34);
INSERT INTO regex_validation(id, creation_date, last_modified, pattern, field_type_id) VALUES (35, now(), now(),'Irvine|Paris|Sydney', 35);
INSERT INTO regex_validation(id, creation_date, last_modified, pattern, field_type_id) VALUES (36, now(), now(),'[0-9]{2}/[0-9]{2}/[0-9]{2,4}', 36);
INSERT INTO regex_validation(id, creation_date, last_modified, pattern, field_type_id) VALUES (37, now(), now(),'998[0-9]{9}', 37);
INSERT INTO regex_validation(id, creation_date, last_modified, pattern, field_type_id) VALUES (38, now(), now(),'[0-9]{1,3},?[0-9]{1,3}\\S[0-9]{2}', 38);
INSERT INTO regex_validation(id, creation_date, last_modified, pattern, field_type_id) VALUES (39, now(), now(),'CA|NY|AZ|NV|NJ', 39);
INSERT INTO regex_validation(id, creation_date, last_modified, pattern, field_type_id) VALUES (40, now(), now(),'Irvine|Paris|Sydney', 40);
INSERT INTO regex_validation(id, creation_date, last_modified, pattern, field_type_id) VALUES (41, now(), now(),'[0-9]{2}/[0-9]{2}/[0-9]{2,4}', 41);
INSERT INTO regex_validation(id, creation_date, last_modified, pattern, field_type_id) VALUES (42, now(), now(),'998[0-9]{9}', 42);
INSERT INTO regex_validation(id, creation_date, last_modified, pattern, field_type_id) VALUES (43, now(), now(),'[0-9]{1,3},?[0-9]{1,3}\\S[0-9]{2}', 43);
INSERT INTO regex_validation(id, creation_date, last_modified, pattern, field_type_id) VALUES (44, now(), now(),'CA|NY|AZ|NV|NJ', 44);
INSERT INTO regex_validation(id, creation_date, last_modified, pattern, field_type_id) VALUES (45, now(), now(),'Irvine|Paris|Sydney', 45);
INSERT INTO regex_validation(id, creation_date, last_modified, pattern, field_type_id) VALUES (46, now(), now(),'[0-9]{2}/[0-9]{2}/[0-9]{2,4}', 46);
INSERT INTO regex_validation(id, creation_date, last_modified, pattern, field_type_id) VALUES (47, now(), now(),'998[0-9]{9}', 47);
INSERT INTO regex_validation(id, creation_date, last_modified, pattern, field_type_id) VALUES (48, now(), now(),'[0-9]{1,3},?[0-9]{1,3}\\S[0-9]{2}', 48);
INSERT INTO regex_validation(id, creation_date, last_modified, pattern, field_type_id) VALUES (49, now(), now(),'CA|NY|AZ|NV|NJ', 49);
INSERT INTO regex_validation(id, creation_date, last_modified, pattern, field_type_id) VALUES (50, now(), now(),'Irvine|Paris|Sydney', 50);

/*Table Info Inserts*/
INSERT INTO table_info(id, creation_date, last_modified, table_name, start_pattern, end_pattern, document_type_id) VALUES (1, now(), now(),'Invoice Table', 'TERMS:', 'DEPOSITS', 9);
INSERT INTO table_info(id, creation_date, last_modified, table_name, start_pattern, end_pattern, document_type_id) VALUES (2, now(), now(),'Invoice T', 'TERMS:', 'asdsadsadasd', 9);

/*Table Columns Info Insaerts*/
INSERT INTO table_columns_info(creation_date, last_modified, column_name, column_pattern, between_left, between_right, table_info_id) VALUES (now(), now(),'ITEM NO.','[0-9]{5,6}', '', '[0-9]{1}', 1);
INSERT INTO table_columns_info(creation_date, last_modified, column_name, column_pattern, between_left, between_right, table_info_id) VALUES (now(), now(),'ORDERED','[0-9]{1}', '[0-9]{5,6}', '', 1);
INSERT INTO table_columns_info(creation_date, last_modified, column_name, column_pattern, between_left, between_right, table_info_id) VALUES (now(), now(),'PRICE', '[0-9]{1,3}\\S[0-9]{2}', '', '[0-9]{1,3}\\S[0-9]{2}', 1);
INSERT INTO table_columns_info(creation_date, last_modified, column_name, column_pattern, between_left, between_right, table_info_id) VALUES (now(), now(),'EXTENSION', '[0-9]{1,3}\\S[0-9]{2}', '[0-9]{1,3}\\S[0-9]{2}', '', 1);
INSERT INTO table_columns_info(creation_date, last_modified, column_name, column_pattern, between_left, between_right, table_info_id) VALUES (now(), now(),'ITEM NO.','[0-9]{5,6}', '', '[0-9]{1}', 2);
INSERT INTO table_columns_info(creation_date, last_modified, column_name, column_pattern, between_left, between_right, table_info_id) VALUES (now(), now(),'ORDERED','[0-9]{1}', '[0-9]{5,6}', '', 2);
INSERT INTO table_columns_info(creation_date, last_modified, column_name, column_pattern, between_left, between_right, table_info_id) VALUES (now(), now(),'PRICE', '[0-9]{1,3}\\S[0-9]{2}', '', '[0-9]{1,3}\\S[0-9]{2}', 2);
INSERT INTO table_columns_info(creation_date, last_modified, column_name, column_pattern, between_left, between_right, table_info_id) VALUES (now(), now(),'EXTENSION', '[0-9]{1,3}\\S[0-9]{2}', '[0-9]{1,3}\\S[0-9]{2}', '', 2);

/*Sample tables/inserts for Fuzzy DB*/
CREATE TABLE us_invoice (id bigint(20) NOT NULL AUTO_INCREMENT, invoice_date varchar(255) DEFAULT NULL,part_number varchar(255) DEFAULT NULL,invoice_total varchar(255) DEFAULT NULL,state varchar(255) DEFAULT NULL,city varchar(255) DEFAULT NULL, PRIMARY KEY (id));
INSERT INTO us_invoice(id, invoice_date, part_number, invoice_total, state, city) VALUES (1,now(),11,100.20,'CA','Irvine');
INSERT INTO us_invoice(id, invoice_date, part_number, invoice_total, state, city) VALUES (2,now(),12,200.20,'CALIFORNIA','APPLICATION');
INSERT INTO us_invoice(id, invoice_date, part_number, invoice_total, state, city) VALUES (3,now(),13,300.20,'expropriation','gurgaon');
 
CREATE TABLE us_invoice_data (id bigint(20) NOT NULL AUTO_INCREMENT, invoice_date varchar(255) DEFAULT NULL, part_number varchar(255) DEFAULT NULL, invoice_total varchar(255) DEFAULT NULL,state varchar(255) DEFAULT NULL, city varchar(255) DEFAULT NULL, PRIMARY KEY (id));
INSERT INTO us_invoice_data(id, invoice_date, part_number, invoice_total, state, city) VALUES (1,now(),11,100.20,'CA','Irvine');
INSERT INTO us_invoice_data(id, invoice_date, part_number, invoice_total, state, city) VALUES (2,now(),12,200.20,'CA','gurgaon');
 
CREATE TABLE application_checklist (id bigint(20) NOT NULL AUTO_INCREMENT,invoice_date varchar(255) DEFAULT NULL, part_number varchar(255) DEFAULT NULL,invoice_total varchar(255) DEFAULT NULL,state varchar(255) DEFAULT NULL, city varchar(255) DEFAULT NULL,PRIMARY KEY (id));
INSERT INTO application_checklist(id, invoice_date, part_number, invoice_total, state, city) VALUES (1,now(),11,100.20,'CALIFORNIA','abc');
INSERT INTO application_checklist(id, invoice_date, part_number, invoice_total, state, city) VALUES (2,now(),12,200.20,'CALIFORNIA','APPLICATION');



/*Sample for KV Page Process*/
INSERT INTO kv_page_process(id,creation_date,last_modified,key_pattern,location,value_pattern,batch_class_plugin_config_id) VALUES (1,now(),now(),'completed','TOP_LEFT','Please',115);
INSERT INTO kv_page_process(id,creation_date,last_modified,key_pattern,location,value_pattern,batch_class_plugin_config_id) VALUES (2,now(),now(),'checklist','TOP_RIGHT','prior',116);
INSERT INTO kv_page_process(id,creation_date,last_modified,key_pattern,location,value_pattern,batch_class_plugin_config_id) VALUES (5,now(),now(),'Belverly','BOTTOM','Irvine',115);
INSERT INTO kv_page_process(id,creation_date,last_modified,key_pattern,location,value_pattern,batch_class_plugin_config_id) VALUES (6,now(),now(),'completed','TOP_LEFT','Please',116);
INSERT INTO kv_page_process(id,creation_date,last_modified,key_pattern,location,value_pattern,batch_class_plugin_config_id) VALUES (8,now(),now(),'the','TOP','help',115);
INSERT INTO kv_page_process(id,creation_date,last_modified,key_pattern,location,value_pattern,batch_class_plugin_config_id) VALUES (9,now(),now(),'checklist','LEFT','completed',115);
INSERT INTO kv_page_process(id,creation_date,last_modified,key_pattern,location,value_pattern,batch_class_plugin_config_id) VALUES (10,now(),now(),'with','RIGHT','beneficial',115);
INSERT INTO kv_page_process(id,creation_date,last_modified,key_pattern,location,value_pattern,batch_class_plugin_config_id) VALUES (11,now(),now(),'deposit','BOTTOM_RIGHT','states',116);
INSERT INTO kv_page_process(id,creation_date,last_modified,key_pattern,location,value_pattern,batch_class_plugin_config_id) VALUES (12,now(),now(),'association','BOTTOM','set',116);
INSERT INTO kv_page_process(id,creation_date,last_modified,key_pattern,location,value_pattern,batch_class_plugin_config_id) VALUES (14,now(),now(),'association','BOTTOM','set',115);
INSERT INTO kv_page_process(id,creation_date,last_modified,key_pattern,location,value_pattern,batch_class_plugin_config_id) VALUES (15,now(),now(),'order','BOTTOM_LEFT','must',116);
INSERT INTO kv_page_process(id,creation_date,last_modified,key_pattern,location,value_pattern,batch_class_plugin_config_id) VALUES (17,now(),now(),'Pa*r*t','BOTTOM','998[0-9]{9}',115);
INSERT INTO kv_page_process(id,creation_date,last_modified,key_pattern,location,value_pattern,batch_class_plugin_config_id) VALUES (18,now(),now(),'Total','RIGHT','[0-9]{1,3},?[0-9]{1,3}\\S[0-9]{2}',115);
INSERT INTO kv_page_process(id,creation_date,last_modified,key_pattern,location,value_pattern,batch_class_plugin_config_id) VALUES (19,now(),now(),'Hills','BOTTOM_LEFT','CA|NY|AZ|NV|NJ',115);
INSERT INTO kv_page_process(id,creation_date,last_modified,key_pattern,location,value_pattern,batch_class_plugin_config_id) VALUES (20,now(),now(),'Belverly','BOTTOM','Irvine',116);
INSERT INTO kv_page_process(id,creation_date,last_modified,key_pattern,location,value_pattern,batch_class_plugin_config_id) VALUES (21,now(),now(),'Date','RIGHT','[0-9]{2}/[0-9]{2}/[0-9]{2,4}',115);
INSERT INTO kv_page_process(id,creation_date,last_modified,key_pattern,location,value_pattern,batch_class_plugin_config_id) VALUES (22,now(),now(),'Pa*r*t','BOTTOM','998[0-9]{9}',116);
INSERT INTO kv_page_process(id,creation_date,last_modified,key_pattern,location,value_pattern,batch_class_plugin_config_id) VALUES (23,now(),now(),'Total','RIGHT','[0-9]{1,3},?[0-9]{1,3}\\S[0-9]{2}',116);

/*Sample for vFBValidation*/
CREATE TABLE vFBValidation(file_id bigint(20) NOT NULL AUTO_INCREMENT,loan_number_value VARCHAR(255) NOT NULL,borrow_fullname VARCHAR(255) NOT NULL,cborrow_fullname VARCHAR(255) NOT NULL,PRIMARY KEY (file_id));
INSERT INTO vFBValidation(file_id, loan_number_value, borrow_fullname, cborrow_fullname) VALUES (1,'abc', 'abc,xyz', 'xyz,zbc');

update kv_page_process set no_of_words = 0 where no_of_words is null ;
update kv_extraction set no_of_words = 0 where no_of_words is null;