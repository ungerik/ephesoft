/* Table Structure Changes */

alter table kv_extraction modify column value_pattern varchar(255) NULL;

alter table kv_extraction modify column multiplier Float;

alter table kv_page_process modify page_level_field_name varchar(100) not null default 'KV_Page_Process';

alter table plugin_config_sample_value ADD UNIQUE (sample_value,plugin_config_id);

alter table kv_extraction modify column location varchar(255) NULL;

/*Create queries.*/

/*creating batch class module config table.*/
CREATE TABLE IF NOT EXISTS batch_class_module_config (
	id BIGINT(20) NOT NULL AUTO_INCREMENT,
	creation_date DATETIME NOT NULL,
	last_modified DATETIME NULL DEFAULT NULL,
	batch_class_module_id BIGINT(20) NULL DEFAULT NULL,
	module_config_id BIGINT(20) NULL DEFAULT NULL,
	PRIMARY KEY (id),
	INDEX FKE4C5156947189631 (batch_class_module_id),
	INDEX FKE4C51569E0DA1978 (module_config_id),
	CONSTRAINT FKE4C5156947189631 FOREIGN KEY (batch_class_module_id) REFERENCES batch_class_module (id),
	CONSTRAINT FKE4C51569E0DA1978 FOREIGN KEY (module_config_id) REFERENCES module_config (id)
);

/*creating the dlf table.*/
CREATE TABLE if not exists `dlf` (
`batch_instance_id` VARCHAR(50) NOT NULL,
`batch_class_id` VARCHAR(50) NOT NULL,
`document_type` VARCHAR(50) NOT NULL,
`document_level_field_name` VARCHAR(50) NOT NULL,
`value` VARCHAR(50) NULL DEFAULT NULL
);

/*Creating Vendors table.*/
CREATE TABLE IF NOT EXISTS `vendors` (
  `BDID` bigint(20) NOT NULL AUTO_INCREMENT,
  `Vendor_ID` varchar(20) DEFAULT NULL,
  `VendorName` varchar(255) DEFAULT NULL,
  `Address` varchar(255) DEFAULT NULL,
  `State` varchar(20) DEFAULT NULL,
  `City` varchar(20) DEFAULT NULL,
  `Zip` varchar(20) DEFAULT NULL,
  `Phone` varchar(20) DEFAULT NULL,
  `email` varchar(20) DEFAULT NULL,
  `webaddress` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`BDID`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=latin1;


/*Creating Mortgage table.*/
CREATE TABLE IF NOT EXISTS `mortgage`(
`id` bigint(20) NOT NULL AUTO_INCREMENT,
`Name` varchar(255) DEFAULT NULL,
`Street_Address` varchar(255) DEFAULT NULL,
`Zip_Code` varchar(255) DEFAULT NULL,
`Phone_Number` varchar(20) DEFAULT NULL,
`SSN` varchar(20) DEFAULT NULL,
`Loan_Number` varchar(20) DEFAULT NULL,
PRIMARY KEY (`id`)
);

/*Delete queries.*/

delete from server_registry where 0 = (select count(*) from service_status);

/*delete queries in plugin config sample values*/
delete from plugin_config_sample_value where sample_value = 'AutomaticClassification' and plugin_config_id in (select id from plugin_config where config_name='da.factory_classification');
delete from plugin_config_sample_value where plugin_config_id in (select id from plugin_config where plugin_config.config_name='regular.regex.extraction_switch');
delete from plugin_config_sample_value where plugin_config_id in (select id from plugin_config where plugin_config.config_name='fuzzydb.stop_words');
delete from plugin_config_sample_value where plugin_config_id in (select id from plugin_config where plugin_config.config_name='csvFileCreation.switch');
delete from plugin_config_sample_value where plugin_config_id in (select id from plugin_config where plugin_config.config_name='ibmCm.switch');
delete from plugin_config_sample_value where plugin_config_id in (select id from plugin_config where plugin_config.config_name='kvextraction.switch');
delete from plugin_config_sample_value where plugin_config_id in (select id from plugin_config where plugin_config.config_name='recostar_extraction.switch');
delete from plugin_config_sample_value where plugin_config_id in (select id from plugin_config where plugin_config.config_name='fuzzydb.switch');
delete from plugin_config_sample_value where plugin_config_id in (select id from plugin_config where plugin_config.config_name='tableextarction.switch');
delete from plugin_config_sample_value where plugin_config_id in (select id from plugin_config where plugin_config.config_name='tabbedPdf.placeholder');
delete from plugin_config_sample_value where plugin_config_id in (select id from plugin_config where plugin_config.config_name='tesseract.switch');
delete from plugin_config_sample_value where plugin_config_id in (select id from plugin_config where plugin_config.config_name='fuzzydb.query_delimiters');
delete from plugin_config_sample_value where plugin_config_id in (select id from plugin_config where plugin_config.config_name='createMultipageTif.export_process');
delete from plugin_config_sample_value where plugin_config_id in (select id from plugin_config where plugin_config.config_name='batch.export_to_folder_switch');
delete from plugin_config_sample_value where plugin_config_id in (select id from plugin_config where plugin_config.config_name='barcode.extraction.valid_extensions');
delete from plugin_config_sample_value where plugin_config_id in (select id from plugin_config where plugin_config.config_name='barcode.extraction.reader_types');
delete from plugin_config_sample_value where plugin_config_id in (select id from plugin_config where plugin_config.config_name='barcode.extraction.min_confidence');
delete from plugin_config_sample_value where plugin_config_id in (select id from plugin_config where plugin_config.config_name='barcode.extraction.max_confidence');
delete from plugin_config_sample_value where plugin_config_id in (select id from plugin_config where plugin_config.config_name='barcode.extraction.switch');
delete from plugin_config_sample_value where plugin_config_id in (select id from plugin_config where plugin_config.config_name='validation.validationScriptSwitch');
delete from plugin_config_sample_value where plugin_config_id in (select id from plugin_config where plugin_config.config_name='da.merge_unknown_document_switch');
delete from plugin_config_sample_value where plugin_config_id in (select id from plugin_config where plugin_config.config_name='filebound.switch');
delete from plugin_config_sample_value where plugin_config_id in (select id from plugin_config where plugin_config.config_name='tesseract.versions');
delete from plugin_config_sample_value where plugin_config_id in (select id from plugin_config where plugin_config.config_name='regular.regex.extraction_switch');
delete from plugin_config_sample_value where sample_value = 'gif' and plugin_config_id = (select id from plugin_config where config_name='folderimporter.valid_extensions');
delete from plugin_config_sample_value where plugin_config_id in (select id from plugin_config where plugin_config.config_name='fuzzydb.search.switch');
delete from plugin_config_sample_value where plugin_config_id in (select id from plugin_config where plugin_config.config_name='review.external_app_switch');

/*deleted in v.3.0.3.2  for removing default value and add html;xml as default value.*/
delete from batch_class_plugin_config where plugin_config_id in (select id from plugin_config where plugin_config.config_name='lucene.valid_extensions');

/*Insert queries.*/

/*Plugin Update*/
INSERT INTO plugin(creation_date,last_modified,plugin_name,plugin_desc,plugin_version,workflow_name) VALUES (now(),now(),'REGULAR_REGEX_EXTRACTION','Regular Regex Extraction Plugin','1.0.0.0','Regular_Regex_Doc_Fields_Extraction_Plugin');
INSERT INTO plugin (creation_date, last_modified, plugin_desc, plugin_name, plugin_version, workflow_name, is_deleted) VALUES (now(), now(), 'NSI Export Plugin', 'NSI_EXPORT', '1.0.0.0', 'NSI_Export_Plugin', 1);
INSERT INTO plugin (creation_date, last_modified, plugin_desc, plugin_name, plugin_version, workflow_name) VALUES (now(), now(), 'Barcode Extraction Plugin', 'BARCODE_EXTRACTION', '1.0.0.0', 'BarCode_Extraction_Plugin');
INSERT INTO plugin (creation_date, last_modified, plugin_desc, plugin_name, plugin_version, workflow_name) VALUES (now(), now(), 'Tabbed PDF Plugin', 'TABBED_PDF', '1.0.0.0', 'Tabbed_Pdf_Plugin');
INSERT INTO plugin (creation_date, last_modified, plugin_desc, plugin_name, plugin_version, workflow_name) VALUES (now(), now(), 'CSV File Creation Plugin', 'CSV_FILE_CREATION_PLUGIN', '1.0.0.0', 'CSV_File_Creation_Plugin');
INSERT INTO plugin (creation_date, last_modified, plugin_desc, plugin_name, plugin_version, workflow_name) VALUES (now(), now(), 'XML for IBM CM Plugin', 'IBM_CM_PLUGIN', '1.0.0.0', 'IBM_CM_Plugin');
INSERT INTO plugin (creation_date, last_modified, plugin_desc, plugin_name, plugin_version, workflow_name) VALUES (now(), now(), 'Key Value Learning Plugin', 'KEY_VALUE_LEARNING_PLUGIN', '1.0.0.0', 'Key_Value_Learning_Plugin');
/*Insert different scripting plugin .*/
INSERT INTO plugin (creation_date, last_modified, plugin_desc, plugin_name, plugin_version, workflow_name) VALUES (now(), now(), 'Export Scripting plugin', 'EXPORT_SCRIPTING_PLUGIN', '1.0.0.0', 'Export_Scripting_Plugin');
INSERT INTO plugin (creation_date, last_modified, plugin_desc, plugin_name, plugin_version, workflow_name) VALUES (now(), now(), 'Extraction Scripting plugin', 'EXTRACTION_SCRIPTING_PLUGIN', '1.0.0.0', 'Extraction_Scripting_Plugin');
INSERT INTO plugin (creation_date, last_modified, plugin_desc, plugin_name, plugin_version, workflow_name) VALUES (now(), now(), 'Document Assembler Scripting plugin', 'DOCUMENT_ASSEMBLER_SCRIPTING_PLUGIN', '1.0.0.0', 'Document_Assembler_Scripting_Plugin');
INSERT INTO plugin (creation_date, last_modified, plugin_desc, plugin_name, plugin_version, workflow_name) VALUES (now(), now(), 'Automated Validation Scripting plugin', 'AUTOMATED_VALIDATION_SCRIPTING_PLUGIN', '1.0.0.0', 'Automated_Validation_Scripting_Plugin');
INSERT INTO plugin (creation_date, last_modified, plugin_desc, plugin_name, plugin_version, workflow_name) VALUES (now(), now(), 'Page Process Scripting plugin', 'PAGE_PROCESS_SCRIPTING_PLUGIN', '1.0.0.0', 'Page_Process_Scripting_Plugin');
/*DB -export plugin entry*/
INSERT INTO plugin (creation_date, last_modified, plugin_desc, plugin_name, script_name, plugin_version, workflow_name) VALUES (now(),now(), 'Database Export Plugin','DB_EXPORT', NULL, '1.0.0.0', 'DB_EXPORT_PLUGIN');


/*Plugin Configs Update*/
INSERT INTO plugin_config(creation_date,last_modified,config_datatype,config_desc,config_multivalue,config_name,plugin_id) VALUES (now(),now(),'STRING','Barcode Extraction Switch',0,'barcode.extraction.switch',null);
INSERT INTO plugin_config(creation_date,last_modified,config_datatype,config_desc,config_multivalue,config_name,plugin_id) VALUES (now(), now(), 'STRING', 'External Application Switch', b'0', 'validation.external_app_switch', NULL);
INSERT INTO plugin_config(creation_date,last_modified,config_datatype,config_desc,config_multivalue,config_name,plugin_id) VALUES (now(),now(),'INTEGER','Barcode Extraction Max Confidence',0,'barcode.extraction.max_confidence',null);
INSERT INTO plugin_config(creation_date,last_modified,config_datatype,config_desc,config_multivalue,config_name,plugin_id) VALUES (now(),now(),'INTEGER','Barcode Extraction Min Confidence',0,'barcode.extraction.min_confidence',null);
INSERT INTO plugin_config(creation_date,last_modified,config_datatype,config_desc,config_multivalue,config_name,plugin_id) VALUES (now(),now(),'STRING','Barcode Extraction Reader Types',1,'barcode.extraction.reader_types',null);
INSERT INTO plugin_config(creation_date,last_modified,config_datatype,config_desc,config_multivalue,config_name,plugin_id) VALUES (now(),now(),'STRING','Barcode Extraction Valid Extensions',1,'barcode.extraction.valid_extensions',null);
INSERT INTO plugin_config(creation_date,last_modified,config_datatype,config_desc,config_multivalue,config_name,plugin_id) VALUES (now(),now(),'STRING','Export To Folder Switch',0,'batch.export_to_folder_switch',null);
INSERT INTO plugin_config(creation_date,last_modified,config_datatype,config_desc,config_multivalue,config_name,plugin_id) VALUES (now(),now(),'STRING','Query Delimiters',0,'fuzzydb.query_delimiters',null);
INSERT INTO plugin_config(creation_date,last_modified,config_datatype,config_desc,config_multivalue,config_name,plugin_id) VALUES (now(),now(),'STRING','Multipage File Export Process',0,'createMultipageTif.export_process',null);
INSERT INTO plugin_config(creation_date,last_modified,config_datatype,config_desc,config_multivalue,config_name,plugin_id) VALUES (now(),now(),'STRING','Colored Output PDF',0,'createMultipageTif.coloured_pdf',null);
INSERT INTO plugin_config(creation_date,last_modified,config_datatype,config_desc,config_multivalue,config_name,plugin_id) VALUES (now(),now(),'STRING','Searchable Output PDF',0,'createMultipageTif.searchable_pdf',null);
INSERT INTO plugin_config(creation_date,last_modified,config_datatype,config_desc,config_multivalue,config_name,plugin_id) VALUES (now(),now(),'STRING','Validation Script Switch',0,'validation.validationScriptSwitch',null);
INSERT INTO plugin_config(creation_date,last_modified,config_datatype,config_desc,config_multivalue,config_name,plugin_id) VALUES (now(),now(),'STRING','DA Merge Unknown Document Switch',0,'da.merge_unknown_document_switch',null);
INSERT INTO plugin_config(creation_date,last_modified,config_datatype,config_desc,config_multivalue,config_name,plugin_id) VALUES (now(),now(),'STRING','Regular Regex Extraction Switch',0,'regular.regex.extraction_switch',null);
INSERT INTO plugin_config(creation_date,last_modified,config_datatype,config_desc,config_multivalue,config_name,plugin_id) VALUES (now(),now(),'STRING','Regular Regex Confidence Score',0,'regular.regex.confidence_score',null);
INSERT INTO plugin_config(creation_date,last_modified,config_datatype,config_desc,config_multivalue,config_name,plugin_id) VALUES (now(),now(),'STRING','FileBound Switch',0,'filebound.switch',null);
INSERT INTO plugin_config(creation_date,last_modified,config_datatype,config_desc,config_multivalue,config_name,plugin_id) VALUES (now(),now(),'STRING','Tesseract Version',0,'tesseract.versions',null);
INSERT INTO plugin_config(creation_date,last_modified,config_datatype,config_desc,config_multivalue,config_name,plugin_id) VALUES (now(), now(), 'STRING', 'NSI Export Folder', '', 'nsi.final_export_folder', NULL);
INSERT INTO plugin_config(creation_date,last_modified,config_datatype,config_desc,config_multivalue,config_name,plugin_id) VALUES (now(), now(), 'STRING', 'NSI State Switch', '', 'nsi.switch', NULL);
INSERT INTO plugin_config(creation_date,last_modified,config_datatype,config_desc,config_multivalue,config_name,plugin_id) VALUES (now(), now(), 'STRING', 'Final NSI XML Name', '', 'nsi.final_xml_name', NULL);
INSERT INTO plugin_config(creation_date,last_modified,config_datatype,config_desc,config_multivalue,config_name,plugin_id) VALUES (now(), now(), 'STRING', 'URL1(Ctrl+4)', 0, 'validation.url(Ctrl+4)', NULL);
INSERT INTO plugin_config(creation_date,last_modified,config_datatype,config_desc,config_multivalue,config_name,plugin_id) VALUES (now(), now(), 'STRING', 'URL1 Title', 0, 'validation.url1_title', NULL);
INSERT INTO plugin_config(creation_date,last_modified,config_datatype,config_desc,config_multivalue,config_name,plugin_id) VALUES (now(), now(), 'STRING', 'URL2(Ctrl+7)', 0, 'validation.url(Ctrl+7)', NULL);
INSERT INTO plugin_config(creation_date,last_modified,config_datatype,config_desc,config_multivalue,config_name,plugin_id) VALUES (now(), now(), 'STRING', 'URL2 Title', 0, 'validation.url2_title', NULL);
INSERT INTO plugin_config(creation_date,last_modified,config_datatype,config_desc,config_multivalue,config_name,plugin_id) VALUES (now(), now(), 'STRING', 'URL3(Ctrl+8)', 0, 'validation.url(Ctrl+8)', NULL);
INSERT INTO plugin_config(creation_date,last_modified,config_datatype,config_desc,config_multivalue,config_name,plugin_id) VALUES (now(), now(), 'STRING', 'URL3 Title', 0, 'validation.url3_title', NULL);
INSERT INTO plugin_config(creation_date,last_modified,config_datatype,config_desc,config_multivalue,config_name,plugin_id) VALUES (now(), now(), 'STRING', 'URL4(Ctrl+9)',0, 'validation.url(Ctrl+9)', NULL);
INSERT INTO plugin_config(creation_date,last_modified,config_datatype,config_desc,config_multivalue,config_name,plugin_id) VALUES (now(), now(), 'STRING', 'URL4 Title', 0, 'validation.url4_title', NULL);
INSERT INTO plugin_config(creation_date,last_modified,config_datatype,config_desc,config_multivalue,config_name,plugin_id) VALUES (now(), now(), 'STRING', 'X Dimension', 0, 'validation.x_dimension', NULL);
INSERT INTO plugin_config(creation_date,last_modified,config_datatype,config_desc,config_multivalue,config_name,plugin_id) VALUES (now(), now(), 'STRING', 'Y Dimension', 0, 'validation.y_dimension', NULL);
INSERT INTO plugin_config(creation_date,last_modified,config_datatype,config_desc,config_multivalue,config_name,plugin_id) VALUES (now(),now(),'STRING','Recostar color switch',0,'recostar.color_switch',null);
INSERT INTO plugin_config(creation_date,last_modified,config_datatype,config_desc,config_multivalue,config_name,plugin_id) VALUES (now(),now(),'STRING','Tesseract color switch',0,'tesseract.color_switch',null);
INSERT INTO plugin_config(creation_date,last_modified,config_datatype,config_desc,config_multivalue,config_name,plugin_id) VALUES (now(),now(),'STRING','Recostar Extarction color switch',0,'recostar_extraction.color_switch',null);
INSERT INTO plugin_config(creation_date,last_modified,config_datatype,config_desc,config_multivalue,config_name,plugin_id) VALUES (now(),now(),'STRING','Recostar Auto Rotate switch',0,'recostar.auto_rotate_switch',null);
INSERT INTO plugin_config(creation_date,last_modified,config_datatype,config_desc,config_multivalue,config_name,plugin_id) VALUES (now(),now(),'STRING','Recostar Auto Rotate switch',0,'recostar_extraction.auto_rotate_switch',null);
INSERT INTO plugin_config(creation_date,last_modified,config_datatype,config_desc,config_multivalue,config_name,plugin_id) VALUES (now(),now(),'STRING','IBM CM Switch',0,'ibmCm.switch',null);
INSERT INTO plugin_config(creation_date,last_modified,config_datatype,config_desc,config_multivalue,config_name,plugin_id) VALUES (now(),now(),'STRING','IBM CM Final Export Folder',0,'ibmCm.final_export_folder',null);
INSERT INTO plugin_config(creation_date,last_modified,config_datatype,config_desc,config_multivalue,config_name,plugin_id) VALUES (now(),now(),'STRING','Recostar Extarction Switch',0,'recostar_extraction.switch',null);
INSERT INTO plugin_config(creation_date,last_modified,config_datatype,config_desc,config_multivalue,config_name,plugin_id) VALUES (now(),now(),'STRING','KV Extraction switch',0,'kvextraction.switch',null);
INSERT INTO plugin_config(creation_date,last_modified,config_datatype,config_desc,config_multivalue,config_name,plugin_id) VALUES (now(),now(),'STRING','FuzzyDB Extraction switch',0,'fuzzydb.switch',null);
INSERT INTO plugin_config(creation_date,last_modified,config_datatype,config_desc,config_multivalue,config_name,plugin_id) VALUES (now(),now(),'STRING','Table Extraction switch',0,'tableextarction.switch',null);
INSERT INTO plugin_config(creation_date,last_modified,config_datatype,config_desc,config_multivalue,config_name,plugin_id) VALUES (now(),now(),'STRING','Tabbed PDF Placeholder',0,'tabbedPdf.placeholder',null);
INSERT INTO plugin_config(creation_date,last_modified,config_datatype,config_desc,config_multivalue,config_name,plugin_id) VALUES (now(),now(),'STRING','Tabbed PDF Property file',0,'tabbedPdf.property_file',null);
INSERT INTO plugin_config(creation_date,last_modified,config_datatype,config_desc,config_multivalue,config_name,plugin_id) VALUES (now(),now(),'STRING','Tesseract Switch',0,'tesseract.switch',null);
INSERT INTO plugin_config(creation_date,last_modified,config_datatype,config_desc,config_multivalue,config_name,plugin_id) VALUES (now(),now(),'STRING','Recostar Switch',0,'recostar.switch',null);
INSERT INTO plugin_config(creation_date,last_modified,config_datatype,config_desc,config_multivalue,config_name,plugin_id) VALUES (now(),now(),'STRING','Tabbed PDF Export Folder',0,'tabbedPdf.final_export_folder',null);
INSERT INTO plugin_config(creation_date,last_modified,config_datatype,config_desc,config_multivalue,config_name,plugin_id) VALUES (now(),now(),'STRING','Tabbed PDF Switch',0,'tabbedPdf.switch',null);
INSERT INTO plugin_config(creation_date,last_modified,config_datatype,config_desc,config_multivalue,config_name,plugin_id) VALUES (now(),now(),'STRING','Filebound index field',0,'filebound.index_field',null);
INSERT INTO plugin_config(creation_date,last_modified,config_datatype,config_desc,config_multivalue,config_name,plugin_id) VALUES (now(),now(),'STRING','Filebound division',0,'filebound.division',null);
INSERT INTO plugin_config(creation_date,last_modified,config_datatype,config_desc,config_multivalue,config_name,plugin_id) VALUES (now(),now(),'STRING','Filebound separator',0,'filebound.separator',null);
INSERT INTO plugin_config(creation_date,last_modified,config_datatype,config_desc,config_multivalue,config_name,plugin_id) VALUES (now(),now(),'STRING','CSV Creation Switch',0,'csvFileCreation.switch',null);
INSERT INTO plugin_config(creation_date,last_modified,config_datatype,config_desc,config_multivalue,config_name,plugin_id) VALUES (now(),now(),'STRING','CSV Creation Final Export Folder',0,'csvFileCreation.final_export_folder',null);
INSERT INTO plugin_config(creation_date,last_modified,config_datatype,config_desc,config_multivalue,config_name,plugin_id) VALUES (now(),now(),'INTEGER','First Page Confidence Score Value',0,'lucene.first_page_conf_weightage',null);
INSERT INTO plugin_config(creation_date,last_modified,config_datatype,config_desc,config_multivalue,config_name,plugin_id) VALUES (now(),now(),'INTEGER','Middle Page Confidence Score Value',0,'lucene.middle_page_conf_weightage',null);
INSERT INTO plugin_config(creation_date,last_modified,config_datatype,config_desc,config_multivalue,config_name,plugin_id) VALUES (now(),now(),'INTEGER','Last Page Confidence Score Value',0,'lucene.last_page_conf_weightage',null);
INSERT INTO plugin_config(creation_date,last_modified,config_datatype,config_desc,config_multivalue,config_name,plugin_id) VALUES (now(),now(),'STRING', 'Field Value Change Script Switch',0, 'validation.field_value_change_script_switch', NULL);
INSERT INTO plugin_config(creation_date,last_modified,config_datatype,config_desc,config_multivalue,config_name,plugin_id) VALUES (now(),now(),'STRING', 'Fuzzy Search Switch',0, 'validation.fuzzy_search_switch', NULL);
INSERT INTO plugin_config(creation_date,last_modified,config_datatype,config_desc,config_multivalue,config_name,plugin_id) VALUES (now(), now(), 'STRING', 'Suggestion box Switch', b'0', 'validation.suggestion_box_switch', NULL);
INSERT INTO plugin_config(creation_date,last_modified,config_datatype,config_desc,config_multivalue,config_name,plugin_id) VALUES (now(),now(),'STRING','Filebound Export Format',0,'filebound.exportformat',null);
INSERT INTO plugin_config(creation_date,last_modified,config_datatype,config_desc,config_multivalue,config_name,plugin_id) VALUES (now(),now(),'STRING','Key Value Learning Switch',0,'keyValueLearning.switch',null);
INSERT INTO plugin_config(creation_date,last_modified,config_datatype,config_desc,is_mandatory,config_multivalue,config_name,plugin_id) VALUES (now(),now(),'STRING','PDF Optimization Parameters',1,'','createMultipageTif.optimization_parameters',NULL);
INSERT INTO plugin_config(creation_date,last_modified,config_datatype,config_desc,config_multivalue,config_name,plugin_id) VALUES (now(),now(),'STRING','Recostar color switch',0,'recostar.color_switch',null);
INSERT INTO plugin_config(creation_date,last_modified,config_datatype,config_desc,config_multivalue,config_name,plugin_id) VALUES (now(),now(),'STRING','GhostScript Image Parameters',0,'ghostscript.image_parameters',null);
INSERT INTO plugin_config(creation_date,last_modified,config_datatype,config_desc,config_multivalue,config_name,plugin_id) VALUES (now(),now(),'STRING','PDF Optimization switch',0,'createMultipageTif.pdf_optimization_switch',NULL);
INSERT INTO plugin_config(creation_date,last_modified,config_datatype,config_desc,config_multivalue,config_name,plugin_id) VALUES (now(),now(),'STRING','PDF Optimization switch',0,'tabbedPdf.pdf_optimization_switch',NULL);
INSERT INTO plugin_config(creation_date,last_modified,config_datatype,config_desc,config_multivalue,config_name,plugin_id) VALUES (now(),now(),'STRING','Create Thumbnails Output Image Parameters',0,'createThumbnails.output_image_parameters',NULL);
INSERT INTO plugin_config(creation_date,last_modified,config_datatype,config_desc,config_multivalue,config_name,plugin_id) VALUES (now(),now(),'STRING','Fuzzy Pop Up X Dimension(in px)',0,'validation.fuzzy_search_pop_up_x_dimension',NULL);
INSERT INTO plugin_config(creation_date,last_modified,config_datatype,config_desc,config_multivalue,config_name,plugin_id) VALUES (now(),now(),'STRING','Fuzzy Pop Up Y Dimension(in px)',0,'validation.fuzzy_search_pop_up_y_dimension',NULL);
INSERT INTO plugin_config(creation_date,last_modified,config_datatype,config_desc,config_multivalue,config_name,plugin_id) VALUES (now(),now(),'STRING','Delete System Folder Information','','cleanup.delete_system_folder_info', NULL);
INSERT INTO plugin_config(creation_date,last_modified,config_datatype,config_desc,config_multivalue,config_name,plugin_id) VALUES (now(),now(),'STRING','Aspect Switch','','cmis.aspects_switch', NULL);
INSERT INTO plugin_config(creation_date,last_modified,config_datatype,config_desc,config_multivalue,config_name,plugin_id) VALUES (now(),now(),'STRING','Barcode Switch','','recostar.barcode_switch', NULL);

/*adding external application switch for review plugin*/
INSERT INTO plugin_config(creation_date,last_modified,config_datatype,config_desc,is_mandatory,config_multivalue,config_name,plugin_id) VALUES (now(), now(), 'STRING', 'External Application Switch',0, 0, 'review.external_app_switch', NULL);
INSERT INTO plugin_config(creation_date,last_modified,config_datatype,config_desc,is_mandatory,config_multivalue,config_name,plugin_id) VALUES (now(), now(), 'STRING', 'URL1(Ctrl+4)',0, 0, 'review.url(Ctrl+4)', NULL);
INSERT INTO plugin_config(creation_date,last_modified,config_datatype,config_desc,is_mandatory,config_multivalue,config_name,plugin_id) VALUES (now(), now(), 'STRING', 'URL1 Title',0, 0, 'review.url1_title', NULL);
INSERT INTO plugin_config(creation_date,last_modified,config_datatype,config_desc,is_mandatory,config_multivalue,config_name,plugin_id) VALUES (now(), now(), 'STRING', 'URL2(Ctrl+7)',0, 0, 'review.url(Ctrl+7)', NULL);
INSERT INTO plugin_config(creation_date,last_modified,config_datatype,config_desc,is_mandatory,config_multivalue,config_name,plugin_id) VALUES (now(), now(), 'STRING', 'URL2 Title',0, 0, 'review.url2_title', NULL);
INSERT INTO plugin_config(creation_date,last_modified,config_datatype,config_desc,is_mandatory,config_multivalue,config_name,plugin_id) VALUES (now(), now(), 'STRING', 'URL3(Ctrl+8)', 0,0, 'review.url(Ctrl+8)', NULL);
INSERT INTO plugin_config(creation_date,last_modified,config_datatype,config_desc,is_mandatory,config_multivalue,config_name,plugin_id) VALUES (now(), now(), 'STRING', 'URL3 Title',0, 0, 'review.url3_title', NULL);
INSERT INTO plugin_config(creation_date,last_modified,config_datatype,config_desc,is_mandatory,config_multivalue,config_name,plugin_id) VALUES (now(), now(), 'STRING', 'URL4(Ctrl+9)',0,0, 'review.url(Ctrl+9)', NULL);
INSERT INTO plugin_config(creation_date,last_modified,config_datatype,config_desc,is_mandatory,config_multivalue,config_name,plugin_id) VALUES (now(), now(), 'STRING', 'URL4 Title', 0, 0,'review.url4_title', NULL);
INSERT INTO plugin_config(creation_date,last_modified,config_datatype,config_desc,is_mandatory,config_multivalue,config_name,plugin_id) VALUES (now(), now(), 'INTEGER', 'X Dimension', 0,0, 'review.x_dimension', NULL);
INSERT INTO plugin_config(creation_date,last_modified,config_datatype,config_desc,is_mandatory,config_multivalue,config_name,plugin_id) VALUES (now(), now(), 'INTEGER', 'Y Dimension', 0,0, 'review.y_dimension', NULL);

/*adding plugin configs for copy batch xml plugin in export module*/
INSERT INTO plugin_config (creation_date, last_modified, config_datatype, config_desc, is_mandatory, config_multivalue, config_name, plugin_id) VALUES (now(), now(), 'STRING', 'Export Folder Name', 0, 0, 'batch.folder_name',  NULL);
INSERT INTO plugin_config (creation_date, last_modified, config_datatype, config_desc, is_mandatory, config_multivalue, config_name, plugin_id) VALUES (now(), now(), 'STRING', 'Export File Name', 0, 0, 'batch.file_name',  NULL);
INSERT INTO plugin_config (creation_date, last_modified, config_datatype, config_desc, is_mandatory, config_multivalue, config_name, plugin_id) VALUES (now(), now(), 'STRING', 'Batch XML Export Folder', 1, 0, 'batch.batch_xml_export_folder',  NULL);

/*adding plugin configs for fuzzy DB plugin in extraction module*/
INSERT INTO plugin_config (creation_date, last_modified, config_datatype, config_desc, is_mandatory, config_multivalue, config_name, plugin_id) VALUES (now(), now(), 'STRING', 'Fuzzy Extraction Search Columns based on Fields', 0, 0, 'fuzzydb.search.columnName',  NULL);

/*adding fuzzydb search switch for fuzzy DB plugin in extraction module*/
INSERT INTO plugin_config (creation_date, last_modified, config_datatype, config_desc, is_mandatory, config_multivalue, config_name, plugin_id) VALUES (now(), now(), 'STRING', 'Fuzzy Extraction HOCR Switch', 0, 0, 'fuzzydb.search.switch',  NULL);

/*adding pdf optimization parameters.*/
insert into plugin_config(creation_date,last_modified,config_datatype,config_desc,config_multivalue,config_name,plugin_id) values (now(),now(),'STRING','PDF Creation Parameters',0,'createMultipageTif.ghostscript_pdf_parameters',null);
insert into plugin_config(creation_date,last_modified,config_datatype,config_desc,config_multivalue,config_name,plugin_id) values (now(),now(),'STRING','Tabbed PDF Creation Parameters',0,'tabbedPdf.creation_parameters',null);
insert into plugin_config(creation_date,last_modified,config_datatype,config_desc,config_multivalue,config_name,plugin_id) values (now(),now(),'STRING','Tabbed PDF Optimization Parameters',0,'tabbedPdf.optimization_parameters',null);

/*DB -export plugin  config entry*/
INSERT INTO plugin_config (creation_date, last_modified, config_datatype, config_desc, is_mandatory, config_multivalue, config_name, order_number, plugin_id, is_deleted) VALUES  (now(), now(), 'STRING', 'Database User Name', true, false, 'dbExport.database.userName', NULL, null, 1);
INSERT INTO plugin_config (creation_date, last_modified, config_datatype, config_desc,is_mandatory, config_multivalue, config_name, order_number, plugin_id, is_deleted) VALUES ( now(), now(), 'STRING', 'Database Password', true, false, 'dbExport.database.password', NULL, null, 1);
INSERT INTO plugin_config (creation_date, last_modified, config_datatype, config_desc, is_mandatory, config_multivalue, config_name, order_number, plugin_id, is_deleted) VALUES ( now(), now(), 'STRING', 'Database Driver', true, false, 'dbExport.database.driver', NULL, null, 1);
INSERT INTO plugin_config (creation_date, last_modified, config_datatype, config_desc, is_mandatory, config_multivalue, config_name, order_number, plugin_id, is_deleted) VALUES ( now(), now(), 'STRING', 'Database Connection URL', true, false, 'dbExport.database.connectionUrl', NULL, null, 1);
INSERT INTO plugin_config (creation_date, last_modified, config_datatype, config_desc, is_mandatory, config_multivalue, config_name, order_number, plugin_id) VALUES ( now(), now(), 'STRING', 'Database Export Switch', true, false, 'dbExport.switch', NULL, null);

/*CMIS export filename plugin config entry.*/
INSERT INTO plugin_config (creation_date,last_modified,config_datatype,config_desc,is_mandatory,config_multivalue,config_name,order_number,plugin_id) VALUES (NOW(),NOW(),'STRING','CMIS Export File Name',0,0,'cmis.file_name',NULL,NULL);

/*Insert query for inserting two properties in IMPORT MULTIPAGE FILES Plugin*/
INSERT INTO plugin_config (creation_date, last_modified, config_datatype, config_desc, is_mandatory, config_multivalue, config_name, order_number, plugin_id) VALUES (now(),now(), 'STRING', 'PDF To TIFF Conversion Process', b'1', b'0', 'folderimporter.pdf_to_tiff_process', NULL, (select id from plugin where plugin_name='IMPORT_MULTIPAGE_FILES'));

/*Plugin Config Sample Values*/
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'tif',(select id from plugin_config where config_name='barcode.extraction.valid_extensions'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'gif',(select id from plugin_config where config_name='barcode.extraction.valid_extensions'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'CODE39',(select id from plugin_config where config_name='barcode.extraction.reader_types'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'CODE93',(select id from plugin_config where config_name='barcode.extraction.reader_types'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'CODE128',(select id from plugin_config where config_name='barcode.extraction.reader_types'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'ITF',(select id from plugin_config where config_name='barcode.extraction.reader_types'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'PDF417',(select id from plugin_config where config_name='barcode.extraction.reader_types'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'QR',(select id from plugin_config where config_name='barcode.extraction.reader_types'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'DATAMATRIX',(select id from plugin_config where config_name='barcode.extraction.reader_types'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'CODABAR',(select id from plugin_config where config_name='barcode.extraction.reader_types'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) values (now(),now(),'EAN13',(select id from plugin_config where config_name='barcode.extraction.reader_types'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'ON',(select id from plugin_config where config_name='barcode.extraction.switch'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'OFF',(select id from plugin_config where config_name='barcode.extraction.switch'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'ON',(select id from plugin_config where config_name='batch.export_to_folder_switch'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'OFF',(select id from plugin_config where config_name='batch.export_to_folder_switch'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'IMAGE_MAGICK',(select id from plugin_config where config_name='createMultipageTif.export_process'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'HOCRtoPDF',(select id from plugin_config where config_name='createMultipageTif.export_process'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'GHOSTSCRIPT',(select id from plugin_config where config_name='createMultipageTif.export_process'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'FALSE',(select id from plugin_config where config_name='createMultipageTif.coloured_pdf'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'TRUE',(select id from plugin_config where config_name='createMultipageTif.coloured_pdf'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'FALSE',(select id from plugin_config where config_name='createMultipageTif.searchable_pdf'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'TRUE',(select id from plugin_config where config_name='createMultipageTif.searchable_pdf'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'OFF',(select id from plugin_config where config_name='validation.validationScriptSwitch'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'AutomaticClassification',(select id from plugin_config where config_name='da.factory_classification'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'OFF',(select id from plugin_config where config_name='da.merge_unknown_document_switch'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'ON',(select id from plugin_config where config_name='da.merge_unknown_document_switch'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'ON',(select id from plugin_config where config_name='regular.regex.extraction_switch'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'OFF',(select id from plugin_config where config_name='regular.regex.extraction_switch'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'OFF',(select id from plugin_config where config_name='filebound.switch'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'ON',(select id from plugin_config where config_name='filebound.switch'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'tesseract_version_3',(select id from plugin_config where config_name='tesseract.versions'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'ON',(select id from plugin_config where config_name='nsi.switch'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'OFF',(select id from plugin_config where config_name='nsi.switch'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'ON',(select id from plugin_config where config_name='recostar.color_switch'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'OFF',(select id from plugin_config where config_name='recostar.color_switch'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'ON',(select id from plugin_config where config_name='tesseract.color_switch'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'OFF',(select id from plugin_config where config_name='tesseract.color_switch'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'ON',(select id from plugin_config where config_name='recostar_extraction.color_switch'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'OFF',(select id from plugin_config where config_name='recostar_extraction.color_switch'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'ON',(select id from plugin_config where config_name='recostar.auto_rotate_switch'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'OFF',(select id from plugin_config where config_name='recostar.auto_rotate_switch'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'ON',(select id from plugin_config where config_name='recostar_extraction.auto_rotate_switch'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'OFF',(select id from plugin_config where config_name='recostar_extraction.auto_rotate_switch'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'png',(select id from plugin_config where config_name='recostar.valid_extensions'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'png',(select id from plugin_config where config_name='tesseract.valid_extensions'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'com.microsoft.jdbc.sqlserver.SQLServerDriver',(select id from plugin_config where config_name='fuzzydb.database.driver'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'net.sourceforge.jtds.jdbc.Driver',(select id from plugin_config where config_name='fuzzydb.database.driver'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'Fpr_MultiLanguage.rsp',(select id from plugin_config where config_name='recostar.project_file'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'name',(select id from plugin_config where config_name='fuzzydb.stop_words'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'title',(select id from plugin_config where config_name='fuzzydb.stop_words'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'png',(select id from plugin_config where config_name='recostar.valid_extensions'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'png',(select id from plugin_config where config_name='tesseract.valid_extensions'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'com.microsoft.jdbc.sqlserver.SQLServerDriver',(select id from plugin_config where config_name='fuzzydb.database.driver'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'FPR_Barcode.rsp',(select id from plugin_config where config_name='recostar.project_file'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'ON',(select id from plugin_config where config_name='recostar_extraction.auto_rotate_switch'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'OFF',(select id from plugin_config where config_name='recostar_extraction.auto_rotate_switch'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'ON',(select id from plugin_config where config_name='validation.external_app_switch'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'OFF',(select id from plugin_config where config_name='validation.external_app_switch'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'ON',(select id from plugin_config where config_name='tesseract.switch'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'OFF',(select id from plugin_config where config_name='tesseract.switch'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'ON',(select id from plugin_config where config_name='recostar.switch'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'OFF',(select id from plugin_config where config_name='recostar.switch'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'ON',(select id from plugin_config where config_name='ibmCm.switch'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'OFF',(select id from plugin_config where config_name='ibmCm.switch'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'YES',(select id from plugin_config where config_name='tabbedPdf.placeholder'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'NO',(select id from plugin_config where config_name='tabbedPdf.placeholder'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'ON',(select id from plugin_config where config_name='tabbedPdf.switch'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'OFF',(select id from plugin_config where config_name='tabbedPdf.switch'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'ON',(select id from plugin_config where config_name='recostar_extraction.switch'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'OFF',(select id from plugin_config where config_name='recostar_extraction.switch'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'ON',(select id from plugin_config where config_name='kvextraction.switch'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'OFF',(select id from plugin_config where config_name='kvextraction.switch'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'ON',(select id from plugin_config where config_name='fuzzydb.switch'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'OFF',(select id from plugin_config where config_name='fuzzydb.switch'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'ON',(select id from plugin_config where config_name='tableextarction.switch'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'OFF',(select id from plugin_config where config_name='tableextarction.switch'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'ON',(select id from plugin_config where config_name='csvFileCreation.switch'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'OFF',(select id from plugin_config where config_name='csvFileCreation.switch'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'ON',(select id from plugin_config where config_name='validation.field_value_change_script_switch'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'OFF',(select id from plugin_config where config_name='validation.field_value_change_script_switch'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'ON',(select id from plugin_config where config_name='validation.fuzzy_search_switch'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'OFF',(select id from plugin_config where config_name='validation.fuzzy_search_switch'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'ON',(select id from plugin_config where config_name='validation.suggestion_box_switch'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'OFF',(select id from plugin_config where config_name='validation.suggestion_box_switch'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'tif',(select id from plugin_config where config_name='filebound.exportformat'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'pdf',(select id from plugin_config where config_name='filebound.exportformat'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'ON',(select id from plugin_config where config_name='keyValueLearning.switch'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'OFF',(select id from plugin_config where config_name='keyValueLearning.switch'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'ON',(select id from plugin_config where config_name='tabbedPdf.pdf_optimization_switch'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'OFF',(select id from plugin_config where config_name='tabbedPdf.pdf_optimization_switch'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'OFF',(select id from plugin_config where config_name='createMultipageTif.pdf_optimization_switch'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'ON',(select id from plugin_config where config_name='createMultipageTif.pdf_optimization_switch'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'TRUE',(select id from plugin_config where config_name='cleanup.delete_system_folder_info'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'FALSE',(select id from plugin_config where config_name='cleanup.delete_system_folder_info'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'PDF417',(select id from plugin_config where config_name='barcode.reader_types'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'CODE128',(select id from plugin_config where config_name='barcode.reader_types'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'CODE93',(select id from plugin_config where config_name='barcode.reader_types'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'ITF',(select id from plugin_config where config_name='barcode.reader_types'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'CODABAR',(select id from plugin_config where config_name='barcode.reader_types'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'EAN13',(select id from plugin_config where config_name='barcode.reader_types'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'ON',(select id from plugin_config where config_name='cmis.aspects_switch'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'OFF',(select id from plugin_config where config_name='cmis.aspects_switch'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'ON',(select id from plugin_config where config_name='recostar.barcode_switch'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'OFF',(select id from plugin_config where config_name='recostar.barcode_switch'));

/*Sample value for review external app plugin configs*/
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'OFF',(select id from plugin_config where config_name='review.external_app_switch'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'ON',(select id from plugin_config where config_name='review.external_app_switch'));

/* Sample values for copy batch Xml plugin configs*/
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'Final Export Folder',(select id from plugin_config where config_name='batch.batch_xml_export_folder'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'Batch Instance Folder',(select id from plugin_config where config_name='batch.batch_xml_export_folder'));

/* Sample values for fuzzydb search switch for fuzzy DB plugin in extraction module*/
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'OFF',(select id from plugin_config where config_name='fuzzydb.search.switch'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'ON',(select id from plugin_config where config_name='fuzzydb.search.switch'));

/*DB -export plugin  config sample value entry*/
INSERT INTO plugin_config_sample_value(creation_date, last_modified, sample_value, plugin_config_id) VALUES ( now(), now(), 'com.mysql.jdbc.Driver', (select id from plugin_config where config_name = 'dbExport.database.driver'));
INSERT INTO plugin_config_sample_value (creation_date, last_modified, sample_value, plugin_config_id) VALUES ( now(), now(), 'net.sourceforge.jtds.jdbc.Driver', (select id from plugin_config where config_name = 'dbExport.database.driver'));
INSERT INTO plugin_config_sample_value (creation_date, last_modified, sample_value, plugin_config_id) VALUES ( now(), now(), 'com.microsoft.jdbc.sqlserver.SQLServerDriver', (select id from plugin_config where config_name = 'dbExport.database.driver'));
INSERT INTO plugin_config_sample_value (creation_date, last_modified, sample_value, plugin_config_id) VALUES ( now(), now(), 'OFF', (select id from plugin_config where config_name = 'dbExport.switch'));
INSERT INTO plugin_config_sample_value (creation_date, last_modified, sample_value, plugin_config_id) VALUES ( now(), now(), 'ON', (select id from plugin_config where config_name = 'dbExport.switch'));

INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'ITEXT-SEARCHABLE',(select id from plugin_config where config_name='createMultipageTif.export_process'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'ITEXT',(select id from plugin_config where config_name='createMultipageTif.export_process'));

/*Insert query for sample values of new added plugins in IMPORT MULTIPAGE FILES Plugin.*/
INSERT INTO plugin_config_sample_value (creation_date, last_modified, sample_value, plugin_config_id) VALUES (now(),now(), 'Ghostscript', (select id from plugin_config where config_name='folderimporter.pdf_to_tiff_process'));


/*Module config update */
INSERT INTO module_config (creation_date,last_modified,child_display_name,child_key,is_mandatory,module_id) VALUES (now(),now(),NULL,NULL,0,NULL);
INSERT INTO module_config (creation_date,last_modified,child_display_name,child_key,is_mandatory,module_id) VALUES (now(),now(),'Plugin Configuration','PluginConfiguration',0,NULL);
INSERT INTO module_config (creation_date,last_modified,child_display_name,child_key,is_mandatory,module_id) VALUES (now(),now(),'Email Accounts','EmailAccounts',0,NULL);
INSERT INTO module_config (creation_date,last_modified,child_display_name,child_key,is_mandatory,module_id) VALUES (now(),now(),NULL,NULL,1,NULL);
INSERT INTO module_config (creation_date,last_modified,child_display_name,child_key,is_mandatory,module_id) VALUES (now(),now(),'Script Page Processing','ScriptPageProcessing',0,NULL);
INSERT INTO module_config (creation_date,last_modified,child_display_name,child_key,is_mandatory,module_id) VALUES (now(),now(),'Learn Index','LearnIndex','',NULL);
INSERT INTO module_config (creation_date,last_modified,child_display_name,child_key,is_mandatory,module_id) VALUES (now(),now(),'Lucene Sample','LuceneSample',1,NULL);
INSERT INTO module_config (creation_date,last_modified,child_display_name,child_key,is_mandatory,module_id) VALUES (now(),now(),'Image Sample','ImageSample',1,NULL);
INSERT INTO module_config (creation_date,last_modified,child_display_name,child_key,is_mandatory,module_id) VALUES (now(),now(),'Script Document Assembler','ScriptDocumentAssembler',0,NULL);
INSERT INTO module_config (creation_date,last_modified,child_display_name,child_key,is_mandatory,module_id) VALUES (now(),now(),'Script Review','ScriptReview',0,NULL);
INSERT INTO module_config (creation_date,last_modified,child_display_name,child_key,is_mandatory,module_id) VALUES (now(),now(),'Script Extraction','ScriptExtraction',0,NULL);
INSERT INTO module_config (creation_date,last_modified,child_display_name,child_key,is_mandatory,module_id) VALUES (now(),now(),'Fuzzy-DB Index','Fuzzy-DBIndex',0,NULL);
INSERT INTO module_config (creation_date,last_modified,child_display_name,child_key,is_mandatory,module_id) VALUES (now(),now(),'Recostar Extraction','RecostarExtraction',0,NULL);
INSERT INTO module_config (creation_date,last_modified,child_display_name,child_key,is_mandatory,module_id) VALUES (now(),now(),'Script Automatic Validation','ScriptAutomaticValidation',0,NULL);
INSERT INTO module_config (creation_date,last_modified,child_display_name,child_key,is_mandatory,module_id) VALUES (now(),now(),'Script Validation','ScriptValidation',0,NULL);
INSERT INTO module_config (creation_date,last_modified,child_display_name,child_key,is_mandatory,module_id) VALUES (now(),now(),'Script Add New Table','ScriptAddNewTable',0,NULL);
INSERT INTO module_config (creation_date,last_modified,child_display_name,child_key,is_mandatory,module_id) VALUES (now(),now(),'Script FunctionKey','ScriptFunctionKey',0,NULL);
INSERT INTO module_config (creation_date,last_modified,child_display_name,child_key,is_mandatory,module_id) VALUES (now(),now(),'Script Export','ScriptExport',0,NULL);
INSERT INTO module_config (creation_date,last_modified,child_display_name,child_key,is_mandatory,module_id) VALUES (now(),now(),'Cmis Mapping','CmisMapping',0,NULL);
INSERT INTO module_config (creation_date,last_modified,child_display_name,child_key,is_mandatory,module_id) VALUES (now(),now(),'Script Field Value Change','ScriptFieldValueChange',0,NULL);

/*Insert into batch class dynamic plugin config table.*/
insert into batch_class_dynamic_plugin_config (id, creation_date, last_modified, config_name, config_desc, plugin_config_value, batch_class_plugin_id, parent_id) select bcpc.id,bcpc.creation_date, bcpc.last_modified, config_name, qualifier, plugin_config_value, batch_class_plugin_id, parent_id from batch_class_plugin_config bcpc, plugin_config pc where bcpc.plugin_config_id=pc.id AND bcpc.qualifier<>'null';

/*Scanner master configuration.*/
INSERT INTO scanner_master_configuration(creation_date, last_modified, description, is_mandatory, config_multivalue, config_name, config_data_type) VALUES (now(), now(), 'Current Pixel Type', 1, 1, 'current_pixel_type', 'Integer');
INSERT INTO scanner_master_configuration(creation_date, last_modified, description, is_mandatory, config_multivalue, config_name, config_data_type) VALUES (now(), now(), 'Bit Depth', 1, null, 'bit_depth', 'Integer');
INSERT INTO scanner_master_configuration(creation_date, last_modified, description, is_mandatory, config_multivalue, config_name, config_data_type) VALUES (now(), now(), 'Bit Depth', 1, null, 'bit_depth', 'Integer');
INSERT INTO scanner_master_configuration(creation_date, last_modified, description, is_mandatory, config_multivalue, config_name, config_data_type) VALUES (now(), now(), 'Multi Transfer', 1, 1, 'multi_transfer', 'Boolean');
INSERT INTO scanner_master_configuration(creation_date, last_modified, description, is_mandatory, config_multivalue, config_name, config_data_type) VALUES (now(), now(), 'Hide UI', 1, 1, 'hide_ui', 'Boolean');
INSERT INTO scanner_master_configuration(creation_date, last_modified, description, is_mandatory, config_multivalue, config_name, config_data_type) VALUES (now(), now(), 'Select Feeder', 1, 1, 'select_feeder', 'Boolean');
INSERT INTO scanner_master_configuration(creation_date, last_modified, description, is_mandatory, config_multivalue, config_name, config_data_type) VALUES (now(), now(), 'Auto Scan', 1, 1, 'auto_scan', 'Boolean');
INSERT INTO scanner_master_configuration(creation_date, last_modified, description, is_mandatory, config_multivalue, config_name, config_data_type) VALUES (now(), now(), 'Enable Duplex', 1, 1, 'enable_duplex', 'Boolean');
INSERT INTO scanner_master_configuration(creation_date, last_modified, description, is_mandatory, config_multivalue, config_name, config_data_type) VALUES (now(), now(), 'Blank Page Mode', 1, null, 'blank_page_mode', 'Integer');
INSERT INTO scanner_master_configuration(creation_date, last_modified, description, is_mandatory, config_multivalue, config_name, config_data_type) VALUES (now(), now(),'Blank Page Threshold', 1, null, 'blank_page_threshold', 'Double'); 
INSERT INTO scanner_master_configuration(creation_date, last_modified, description, is_mandatory, config_multivalue, config_name, config_data_type) VALUES (now(), now(), 'DPI', 1, null, 'dpi', 'Integer'); 
INSERT INTO scanner_master_configuration(creation_date, last_modified, description, is_mandatory, config_multivalue, config_name, config_data_type) VALUES (now(), now(), 'Color', 1, null, 'color', 'Integer'); 
INSERT INTO scanner_master_configuration(creation_date, last_modified, description, is_mandatory, config_multivalue, config_name, config_data_type) VALUES (now(), now(), 'Paper Size', 1, null, 'paper_size', 'Integer');
INSERT INTO scanner_master_configuration(creation_date, last_modified, description, is_mandatory, config_multivalue, config_name, config_data_type) VALUES (now(), now(), 'Back Page Rotation Multiple', 1, null, 'back_page_rotation_multiple', 'Integer');
INSERT INTO scanner_master_configuration(creation_date, last_modified, description, is_mandatory, config_multivalue, config_name, config_data_type) VALUES (now(), now(), 'Pages Cache Clear Count', 1, null, 'pages_cache_clear_coun', 'Integer');
INSERT INTO scanner_master_configuration(creation_date, last_modified, description, is_mandatory, config_multivalue, config_name, config_data_type) VALUES (now(), now(), 'Profile Name', 1, null, 'profileName', 'String');
INSERT INTO scanner_master_configuration(creation_date, last_modified, description, is_mandatory, config_multivalue, config_name, config_data_type) VALUES (now(), now(), 'Front Page Rotation Multiple', 1, null, 'front_page_rotation_multiple', 'Integer');


/*Altering the table for scanner master config sample values for including unique key constraint.*/
delete from scanner_master_config_sample_value;

alter table scanner_master_config_sample_value add unique (sample_value, master_config_id);

/*Scanner master configuration sample values.*/
INSERT INTO scanner_master_config_sample_value (creation_date, last_modified, sample_value, master_config_id) VALUES ( now(), now(), '0', (select id from scanner_master_configuration where config_name='current_pixel_type') ); 
INSERT INTO scanner_master_config_sample_value (creation_date, last_modified, sample_value, master_config_id) VALUES (now(), now(), '1', (select id from scanner_master_configuration where config_name='current_pixel_type')); 
INSERT INTO scanner_master_config_sample_value (creation_date, last_modified, sample_value, master_config_id) VALUES (now(), now(), '2', (select id from scanner_master_configuration where config_name='current_pixel_type'));
INSERT INTO scanner_master_config_sample_value (creation_date, last_modified, sample_value, master_config_id) VALUES (now(), now(), '3', (select id from scanner_master_configuration where config_name='current_pixel_type')); 
INSERT INTO scanner_master_config_sample_value (creation_date, last_modified, sample_value, master_config_id) VALUES ( now(), now(), '4', (select id from scanner_master_configuration where config_name='current_pixel_type'));
INSERT INTO scanner_master_config_sample_value (creation_date, last_modified, sample_value, master_config_id) VALUES (now(), now(), 'true', (select id from scanner_master_configuration where config_name='multi_transfer'));
INSERT INTO scanner_master_config_sample_value (creation_date, last_modified, sample_value, master_config_id) VALUES (now(), now(),'false', (select id from scanner_master_configuration where config_name='multi_transfer')); 
INSERT INTO scanner_master_config_sample_value (creation_date, last_modified, sample_value, master_config_id) VALUES (now(), now(), 'true', (select id from scanner_master_configuration where config_name='hide_ui'));
INSERT INTO scanner_master_config_sample_value (creation_date, last_modified, sample_value, master_config_id) VALUES (now(), now(), 'false', (select id from scanner_master_configuration where config_name='hide_ui')); 
INSERT INTO scanner_master_config_sample_value (creation_date, last_modified, sample_value, master_config_id) VALUES (now(), now(),'true', (select id from scanner_master_configuration where config_name='select_feeder')); 
INSERT INTO scanner_master_config_sample_value (creation_date, last_modified, sample_value, master_config_id) VALUES (now(), now(), 'false', (select id from scanner_master_configuration where config_name='select_feeder')); 
INSERT INTO scanner_master_config_sample_value (creation_date, last_modified, sample_value, master_config_id) VALUES (now(), now(), 'true', (select id from scanner_master_configuration where config_name='auto_scan')); 
INSERT INTO scanner_master_config_sample_value (creation_date, last_modified, sample_value, master_config_id) VALUES (now(), now(), 'false',(select id from scanner_master_configuration where config_name='auto_scan'));
INSERT INTO scanner_master_config_sample_value (creation_date, last_modified, sample_value, master_config_id) VALUES (now(), now(), 'true', (select id from scanner_master_configuration where config_name='enable_duplex')); 
INSERT INTO scanner_master_config_sample_value (creation_date, last_modified, sample_value, master_config_id) VALUES (now(), now(),'false', (select id from scanner_master_configuration where config_name='enable_duplex'));
INSERT INTO scanner_master_config_sample_value (creation_date, last_modified, sample_value, master_config_id) VALUES (now(), now(), '150', (select id from scanner_master_configuration where config_name='dpi')); 
INSERT INTO scanner_master_config_sample_value (creation_date, last_modified, sample_value, master_config_id) VALUES (now(), now(), '200', (select id from scanner_master_configuration where config_name='dpi')); 
INSERT INTO scanner_master_config_sample_value (creation_date, last_modified, sample_value, master_config_id) VALUES (now(), now(), '300', (select id from scanner_master_configuration where config_name='dpi'));
INSERT INTO scanner_master_config_sample_value (creation_date, last_modified, sample_value, master_config_id) VALUES (now(), now(), '0', (select id from scanner_master_configuration where config_name='color'));
INSERT INTO scanner_master_config_sample_value (creation_date, last_modified, sample_value, master_config_id) VALUES (now(), now(), '1', (select id from scanner_master_configuration where config_name='color'));
INSERT INTO scanner_master_config_sample_value (creation_date, last_modified, sample_value, master_config_id) VALUES (now(), now(), '2', (select id from scanner_master_configuration where config_name='color'));
INSERT INTO scanner_master_config_sample_value (creation_date, last_modified, sample_value, master_config_id) VALUES (now(), now(), '3', (select id from scanner_master_configuration where config_name='color'));
INSERT INTO scanner_master_config_sample_value (creation_date, last_modified, sample_value, master_config_id) VALUES (now(), now(), '4', (select id from scanner_master_configuration where config_name='color'));
INSERT INTO scanner_master_config_sample_value (creation_date, last_modified, sample_value, master_config_id) VALUES (now(), now(), '0', (select id from scanner_master_configuration where config_name='paper_size'));
INSERT INTO scanner_master_config_sample_value (creation_date, last_modified, sample_value, master_config_id) VALUES (now(), now(), '1', (select id from scanner_master_configuration where config_name='paper_size'));
INSERT INTO scanner_master_config_sample_value (creation_date, last_modified, sample_value, master_config_id) VALUES (now(), now(), '2', (select id from scanner_master_configuration where config_name='paper_size'));
INSERT INTO scanner_master_config_sample_value (creation_date, last_modified, sample_value, master_config_id) VALUES (now(), now(), '3', (select id from scanner_master_configuration where config_name='paper_size'));
INSERT INTO scanner_master_config_sample_value (creation_date, last_modified, sample_value, master_config_id) VALUES (now(), now(), '4', (select id from scanner_master_configuration where config_name='paper_size'));
INSERT INTO scanner_master_config_sample_value (creation_date, last_modified, sample_value, master_config_id) VALUES (now(), now(), '5', (select id from scanner_master_configuration where config_name='paper_size'));
INSERT INTO scanner_master_config_sample_value (creation_date, last_modified, sample_value, master_config_id) VALUES (now(), now(), '6', (select id from scanner_master_configuration where config_name='paper_size'));
INSERT INTO scanner_master_config_sample_value (creation_date, last_modified, sample_value, master_config_id) VALUES (now(), now(), '7', (select id from scanner_master_configuration where config_name='paper_size'));
INSERT INTO scanner_master_config_sample_value (creation_date, last_modified, sample_value, master_config_id) VALUES (now(), now(), '8', (select id from scanner_master_configuration where config_name='paper_size'));
INSERT INTO scanner_master_config_sample_value (creation_date, last_modified, sample_value, master_config_id) VALUES (now(), now(), '9', (select id from scanner_master_configuration where config_name='paper_size'));
INSERT INTO scanner_master_config_sample_value (creation_date, last_modified, sample_value, master_config_id) VALUES (now(), now(), '10', (select id from scanner_master_configuration where config_name='paper_size'));
INSERT INTO scanner_master_config_sample_value (creation_date, last_modified, sample_value, master_config_id) VALUES (now(), now(), '11', (select id from scanner_master_configuration where config_name='paper_size'));
INSERT INTO scanner_master_config_sample_value (creation_date, last_modified, sample_value, master_config_id) VALUES (now(), now(), '12', (select id from scanner_master_configuration where config_name='paper_size'));
INSERT INTO scanner_master_config_sample_value (creation_date, last_modified, sample_value, master_config_id) VALUES (now(), now(), '13', (select id from scanner_master_configuration where config_name='paper_size'));
INSERT INTO scanner_master_config_sample_value (creation_date, last_modified, sample_value, master_config_id) VALUES (now(), now(), '14', (select id from scanner_master_configuration where config_name='paper_size'));
INSERT INTO scanner_master_config_sample_value (creation_date, last_modified, sample_value, master_config_id) VALUES(now(), now(), '15', (select id from scanner_master_configuration where config_name='paper_size'));
INSERT INTO scanner_master_config_sample_value (creation_date, last_modified, sample_value, master_config_id) VALUES(now(), now(), '16', (select id from scanner_master_configuration where config_name='paper_size'));
INSERT INTO scanner_master_config_sample_value (creation_date, last_modified, sample_value, master_config_id) VALUES(now(), now(), '17', (select id from scanner_master_configuration where config_name='paper_size'));
INSERT INTO scanner_master_config_sample_value (creation_date, last_modified, sample_value, master_config_id) VALUES(now(), now(), '18', (select id from scanner_master_configuration where config_name='paper_size'));
INSERT INTO scanner_master_config_sample_value (creation_date, last_modified, sample_value, master_config_id) VALUES(now(), now(), '19', (select id from scanner_master_configuration where config_name='paper_size'));
INSERT INTO scanner_master_config_sample_value (creation_date, last_modified, sample_value, master_config_id) VALUES(now(), now(), '20', (select id from scanner_master_configuration where config_name='paper_size'));
INSERT INTO scanner_master_config_sample_value (creation_date, last_modified, sample_value, master_config_id) VALUES(now(), now(), '21', (select id from scanner_master_configuration where config_name='paper_size'));
INSERT INTO scanner_master_config_sample_value (creation_date, last_modified, sample_value, master_config_id) VALUES(now(), now(), '22', (select id from scanner_master_configuration where config_name='paper_size'));
INSERT INTO scanner_master_config_sample_value (creation_date, last_modified, sample_value, master_config_id) VALUES(now(), now(), '23', (select id from scanner_master_configuration where config_name='paper_size'));
INSERT INTO scanner_master_config_sample_value (creation_date, last_modified, sample_value, master_config_id) VALUES(now(), now(), '24', (select id from scanner_master_configuration where config_name='paper_size'));
INSERT INTO scanner_master_config_sample_value (creation_date, last_modified, sample_value, master_config_id) VALUES(now(), now(), '25', (select id from scanner_master_configuration where config_name='paper_size'));
INSERT INTO scanner_master_config_sample_value (creation_date, last_modified, sample_value, master_config_id) VALUES(now(), now(), '26', (select id from scanner_master_configuration where config_name='paper_size'));
INSERT INTO scanner_master_config_sample_value (creation_date, last_modified, sample_value, master_config_id) VALUES(now(), now(), '27', (select id from scanner_master_configuration where config_name='paper_size'));
INSERT INTO scanner_master_config_sample_value (creation_date, last_modified, sample_value, master_config_id) VALUES(now(), now(), '28', (select id from scanner_master_configuration where config_name='paper_size'));
INSERT INTO scanner_master_config_sample_value (creation_date, last_modified, sample_value, master_config_id) VALUES(now(), now(), '29', (select id from scanner_master_configuration where config_name='paper_size'));
INSERT INTO scanner_master_config_sample_value (creation_date, last_modified, sample_value, master_config_id) VALUES(now(), now(), '30', (select id from scanner_master_configuration where config_name='paper_size'));
INSERT INTO scanner_master_config_sample_value (creation_date, last_modified, sample_value, master_config_id) VALUES(now(), now(), '31', (select id from scanner_master_configuration where config_name='paper_size'));
INSERT INTO scanner_master_config_sample_value (creation_date, last_modified, sample_value, master_config_id) VALUES(now(), now(), '32', (select id from scanner_master_configuration where config_name='paper_size'));
INSERT INTO scanner_master_config_sample_value (creation_date, last_modified, sample_value, master_config_id) VALUES(now(), now(), '33', (select id from scanner_master_configuration where config_name='paper_size'));
INSERT INTO scanner_master_config_sample_value (creation_date, last_modified, sample_value, master_config_id) VALUES(now(), now(), '34', (select id from scanner_master_configuration where config_name='paper_size'));
INSERT INTO scanner_master_config_sample_value (creation_date, last_modified, sample_value, master_config_id) VALUES(now(), now(), '35', (select id from scanner_master_configuration where config_name='paper_size'));
INSERT INTO scanner_master_config_sample_value (creation_date, last_modified, sample_value, master_config_id) VALUES(now(), now(), '36', (select id from scanner_master_configuration where config_name='paper_size'));
INSERT INTO scanner_master_config_sample_value (creation_date, last_modified, sample_value, master_config_id) VALUES(now(), now(), '37', (select id from scanner_master_configuration where config_name='paper_size'));
INSERT INTO scanner_master_config_sample_value (creation_date, last_modified, sample_value, master_config_id) VALUES(now(), now(), '38', (select id from scanner_master_configuration where config_name='paper_size'));
INSERT INTO scanner_master_config_sample_value (creation_date, last_modified, sample_value, master_config_id) VALUES(now(), now(), '39', (select id from scanner_master_configuration where config_name='paper_size'));
INSERT INTO scanner_master_config_sample_value (creation_date, last_modified, sample_value, master_config_id) VALUES(now(), now(), '40', (select id from scanner_master_configuration where config_name='paper_size'));
INSERT INTO scanner_master_config_sample_value (creation_date, last_modified, sample_value, master_config_id) VALUES(now(), now(), '41', (select id from scanner_master_configuration where config_name='paper_size'));
INSERT INTO scanner_master_config_sample_value (creation_date, last_modified, sample_value, master_config_id) VALUES(now(), now(), '42', (select id from scanner_master_configuration where config_name='paper_size'));
INSERT INTO scanner_master_config_sample_value (creation_date, last_modified, sample_value, master_config_id) VALUES(now(), now(), '43', (select id from scanner_master_configuration where config_name='paper_size'));
INSERT INTO scanner_master_config_sample_value (creation_date, last_modified, sample_value, master_config_id) VALUES(now(), now(), '44', (select id from scanner_master_configuration where config_name='paper_size'));
INSERT INTO scanner_master_config_sample_value (creation_date, last_modified, sample_value, master_config_id) VALUES(now(), now(), '45', (select id from scanner_master_configuration where config_name='paper_size'));
INSERT INTO scanner_master_config_sample_value (creation_date, last_modified, sample_value, master_config_id) VALUES(now(), now(), '46', (select id from scanner_master_configuration where config_name='paper_size'));
INSERT INTO scanner_master_config_sample_value (creation_date, last_modified, sample_value, master_config_id) VALUES(now(), now(), '47', (select id from scanner_master_configuration where config_name='paper_size'));
INSERT INTO scanner_master_config_sample_value (creation_date, last_modified, sample_value, master_config_id) VALUES(now(), now(), '48', (select id from scanner_master_configuration where config_name='paper_size'));
INSERT INTO scanner_master_config_sample_value (creation_date, last_modified, sample_value, master_config_id) VALUES(now(), now(), '49', (select id from scanner_master_configuration where config_name='paper_size'));
INSERT INTO scanner_master_config_sample_value (creation_date, last_modified, sample_value, master_config_id) VALUES(now(), now(), '50', (select id from scanner_master_configuration where config_name='paper_size'));
INSERT INTO scanner_master_config_sample_value (creation_date, last_modified, sample_value, master_config_id) VALUES(now(), now(), '51', (select id from scanner_master_configuration where config_name='paper_size'));
INSERT INTO scanner_master_config_sample_value (creation_date, last_modified, sample_value, master_config_id) VALUES(now(), now(), '52', (select id from scanner_master_configuration where config_name='paper_size'));
INSERT INTO scanner_master_config_sample_value (creation_date, last_modified, sample_value, master_config_id) VALUES(now(), now(), '53', (select id from scanner_master_configuration where config_name='paper_size'));
INSERT INTO scanner_master_config_sample_value (creation_date, last_modified, sample_value, master_config_id) VALUES(now(), now(), '0', (select id from scanner_master_configuration where config_name='back_page_rotation_multiple'));
INSERT INTO scanner_master_config_sample_value (creation_date, last_modified, sample_value, master_config_id) VALUES(now(), now(), '90', (select id from scanner_master_configuration where config_name='back_page_rotation_multiple'));
INSERT INTO scanner_master_config_sample_value (creation_date, last_modified, sample_value, master_config_id) VALUES(now(), now(), '180', (select id from scanner_master_configuration where config_name='back_page_rotation_multiple'));
INSERT INTO scanner_master_config_sample_value (creation_date, last_modified, sample_value, master_config_id) VALUES(now(), now(), '270', (select id from scanner_master_configuration where config_name='back_page_rotation_multiple'));
INSERT INTO scanner_master_config_sample_value (creation_date, last_modified, sample_value, master_config_id) VALUES(now(), now(), '0', (select id from scanner_master_configuration where config_name='front_page_rotation_multiple'));
INSERT INTO scanner_master_config_sample_value (creation_date, last_modified, sample_value, master_config_id) VALUES(now(), now(), '90', (select id from scanner_master_configuration where config_name='front_page_rotation_multiple'));
INSERT INTO scanner_master_config_sample_value (creation_date, last_modified, sample_value, master_config_id) VALUES(now(), now(), '180', (select id from scanner_master_configuration where config_name='front_page_rotation_multiple'));
INSERT INTO scanner_master_config_sample_value (creation_date, last_modified, sample_value, master_config_id) VALUES(now(), now(), '270', (select id from scanner_master_configuration where config_name='front_page_rotation_multiple'));

/*Insert into vendors table.*/
INSERT INTO vendors (BDID, Vendor_ID, VendorName, Address, State, City, Zip, Phone, email, webaddress) VALUES(1, '11111', 'Staples', '111 Figuroa', 'CA', 'Los Angeles', '900040', '310-555-5555', NULL, NULL);
INSERT INTO vendors (BDID, Vendor_ID, VendorName, Address, State, City, Zip, Phone, email, webaddress) VALUES(2, '22222', 'Office Depot', '222 Washington', 'DC', 'Wasington', '10000', '222-222-2222', NULL, NULL);
INSERT INTO vendors (BDID, Vendor_ID, VendorName, Address, State, City, Zip, Phone, email, webaddress) VALUES(3, '33333', 'ACME', 'Beverly Hills blvd', 'CA', 'Irvine', '90210', '949-331-7500', NULL, NULL);
INSERT INTO vendors (BDID, Vendor_ID, VendorName, Address, State, City, Zip, Phone, email, webaddress) VALUES(4, '44444', 'RAYA DESIGNS', '711  EAST BALL ROAD', 'CA', 'ANAHEIM', '92805', '714-776-7252', NULL, NULL);
INSERT INTO vendors (BDID, Vendor_ID, VendorName, Address, State, City, Zip, Phone, email, webaddress) VALUES(5, '55555', 'Staffmark', 'P O Box 952386', 'MO', 'St. Luis', '63195', NULL, NULL, NULL);
INSERT INTO vendors (BDID, Vendor_ID, VendorName, Address, State, City, Zip, Phone, email, webaddress) VALUES(6, '66666', 'ISTA', '7825 BAYMEADOWS WAY', 'FL', 'JACKSONWILLE', '32256', NULL, NULL, NULL);
INSERT INTO vendors (BDID, Vendor_ID, VendorName, Address, State, City, Zip, Phone, email, webaddress) VALUES(7, '77777', 'ACSC', NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO vendors (BDID, Vendor_ID, VendorName, Address, State, City, Zip, Phone, email, webaddress) VALUES(8, '88888', 'packstar', '4114 W. VALLEY BLVD', NULL, 'WALNUT', '91789', NULL, NULL, NULL);
INSERT INTO vendors (BDID, Vendor_ID, VendorName, Address, State, City, Zip, Phone, email, webaddress) VALUES(9, '99999', 'COX COMMUNICATIONS', 'P.O. BOX 79173', 'AZ', 'PHEONIX', '85062', NULL, NULL, NULL);
INSERT INTO vendors (BDID, Vendor_ID, VendorName, Address, State, City, Zip, Phone, email, webaddress) VALUES(10, '00001', 'UNIT PACK CO. INC.', NULL, NULL, NULL, NULL, NULL, NULL, NULL);

/*Insert into mortgage table.*/
INSERT INTO mortgage (id,Name,Street_Address, Zip_Code, Phone_Number, SSN, Loan_Number)
VALUES(1,'Adrienne Cooke','920-8360 Mauris St.','9710','624-114-3949','908-37-1749','WB-438516');
INSERT INTO mortgage (id,Name,Street_Address, Zip_Code, Phone_Number, SSN, Loan_Number)
VALUES(2,'Imelda Whitley','7992 Nam Ave','61217','784-958-6213','829-66-7570','DH-116144');
INSERT INTO mortgage (id,Name,Street_Address, Zip_Code, Phone_Number, SSN, Loan_Number)
VALUES(3,'Dillon Terrell','799-4650 Amet, Rd.','80851','127-673-4861','534-46-7414','VQ-671885');
INSERT INTO mortgage (id,Name,Street_Address, Zip_Code, Phone_Number, SSN, Loan_Number)
VALUES(4,'Lev Glover','286-9482 Purus, Av.','3761','542-515-7290','887-07-9587','AR-006877');
INSERT INTO mortgage (id,Name,Street_Address, Zip_Code, Phone_Number, SSN, Loan_Number)
VALUES(5,'Bernard Leon','5746 Duis Avenue','61330','016-861-4342','409-07-2190','RJ-624852');
INSERT INTO mortgage (id,Name,Street_Address, Zip_Code, Phone_Number, SSN, Loan_Number)
VALUES(6,'Kirby Witt','Ap #242-5890 Curabitur Street','42991','493-476-8445','637-15-7396','KR-037522');
INSERT INTO mortgage (id,Name,Street_Address, Zip_Code, Phone_Number, SSN, Loan_Number)
VALUES(7,'Gavin Jones','P.O. Box 165, 480 In Ave','3755','611-732-1381','197-76-9197','HE-250402');
INSERT INTO mortgage (id,Name,Street_Address, Zip_Code, Phone_Number, SSN, Loan_Number)
VALUES(8,'Minerva Oneill','715-340 Tincidunt Ave','63821','012-619-9318','081-29-7956','MB-471198');
INSERT INTO mortgage (id,Name,Street_Address, Zip_Code, Phone_Number, SSN, Loan_Number)
VALUES(9,'Dara Rocha','347-4765 Tempus, Av.','5821','746-181-3383','611-05-4715','YQ-713080');
INSERT INTO mortgage (id,Name,Street_Address, Zip_Code, Phone_Number, SSN, Loan_Number)
VALUES(10,'Yvonne Hughes','P.O. Box 724, 4239 Sed St.','9475','648-483-5893','008-51-1546','SH-478539');
INSERT INTO mortgage (id,Name,Street_Address, Zip_Code, Phone_Number, SSN, Loan_Number)
VALUES(11,'Mariam Stanton','403-6602 Nibh Av.','6137','432-547-1049','398-63-2285','OH-573543');
INSERT INTO mortgage (id,Name,Street_Address, Zip_Code, Phone_Number, SSN, Loan_Number)
VALUES(12,'Brenden Dean','P.O. Box 299, 7520 Sollicitudin St.','56611','084-927-0386','676-65-2893','SZ-670127');
INSERT INTO mortgage (id,Name,Street_Address, Zip_Code, Phone_Number, SSN, Loan_Number)
VALUES(13,'Rina Sharp','4861 Montes, St.','8540','497-773-6949','686-55-3217','FP-325533');
INSERT INTO mortgage (id,Name,Street_Address, Zip_Code, Phone_Number, SSN, Loan_Number)
VALUES(14,'Emery Howard','960-2614 Ipsum Ave','B1S 4S2','079-168-1915','346-06-4874','DL-528517');
INSERT INTO mortgage (id,Name,Street_Address, Zip_Code, Phone_Number, SSN, Loan_Number)
VALUES(15,'Reagan Dalton','6207 Nisl Avenue','29545-498','851-820-4659','385-28-3263','KK-769939');
INSERT INTO mortgage (id,Name,Street_Address, Zip_Code, Phone_Number, SSN, Loan_Number)
VALUES(16,'Byron Osborne','Ap #166-3630 Venenatis Street','7886','196-287-6182','396-95-4633','TP-230983');
INSERT INTO mortgage (id,Name,Street_Address, Zip_Code, Phone_Number, SSN, Loan_Number)
VALUES(17,'Kyle Garrison','314-2459 Elit Street','H3K 7C5','223-997-0166','869-62-7808','AU-551816');
INSERT INTO mortgage (id,Name,Street_Address, Zip_Code, Phone_Number, SSN, Loan_Number)
VALUES(18,'Wayne Bass','629-7837 Nec Av.','24201','501-247-8014','131-27-3938','FC-535422');
INSERT INTO mortgage (id,Name,Street_Address, Zip_Code, Phone_Number, SSN, Loan_Number)
VALUES(19,'Naida Leon','5223 A Av.','5713','112-032-5570','373-45-8796','YY-081665');
INSERT INTO mortgage (id,Name,Street_Address, Zip_Code, Phone_Number, SSN, Loan_Number)
VALUES(20,'Zorita Ortega','P.O. Box 719, 1274 Semper Avenue','54392','252-938-7938','366-27-0117','CK-056989');
INSERT INTO mortgage (id,Name,Street_Address, Zip_Code, Phone_Number, SSN, Loan_Number)
VALUES(21,'Dennis Cantrell','Ap #160-1338 Mauris Ave','P6K 2C6','268-460-3756','477-33-9491','HM-121892');
INSERT INTO mortgage (id,Name,Street_Address, Zip_Code, Phone_Number, SSN, Loan_Number)
VALUES(22,'Wing Gomez','P.O. Box 246, 7735 Quam. Avenue','4623','549-943-2968','172-98-4625','EU-981191');
INSERT INTO mortgage (id,Name,Street_Address, Zip_Code, Phone_Number, SSN, Loan_Number)
VALUES(23,'Vincent Boone','8097 Sed Ave','97569','443-553-4752','752-29-9372','HU-808318');
INSERT INTO mortgage (id,Name,Street_Address, Zip_Code, Phone_Number, SSN, Loan_Number)
VALUES(24,'Tobias Lane','Ap #479-6971 Integer Street','8870SE','972-618-7596','829-72-0892','ZW-775064');
INSERT INTO mortgage (id,Name,Street_Address, Zip_Code, Phone_Number, SSN, Loan_Number)
VALUES(25,'Quail Cruz','1101 Odio Av.','79962','858-693-6935','384-42-6134','IU-329300');
INSERT INTO mortgage (id,Name,Street_Address, Zip_Code, Phone_Number, SSN, Loan_Number)
VALUES(26,'Lee Gill','P.O. Box 446, 207 Ridiculus Road','9571','353-371-5946','390-80-5057','WU-227671');
INSERT INTO mortgage (id,Name,Street_Address, Zip_Code, Phone_Number, SSN, Loan_Number)
VALUES(27,'Len Rogers','9128 Nulla Rd.','91243','337-024-9648','007-92-0880','AR-833799');
INSERT INTO mortgage (id,Name,Street_Address, Zip_Code, Phone_Number, SSN, Loan_Number)
VALUES(28,'Rosalyn Lawrence','Ap #169-7942 Sed Road','1290HA','833-874-1727','748-15-8383','WO-958435');
INSERT INTO mortgage (id,Name,Street_Address, Zip_Code, Phone_Number, SSN, Loan_Number)
VALUES(29,'Barclay Frost','P.O. Box 300, 1792 Dui. Avenue','94640','195-116-5755','888-53-6132','FX-507539');
INSERT INTO mortgage (id,Name,Street_Address, Zip_Code, Phone_Number, SSN, Loan_Number)
VALUES(30,'Zachary Mccullough','4703 Inceptos Ave','7420','942-738-9189','341-61-9090','IX-299310');
INSERT INTO mortgage (id,Name,Street_Address, Zip_Code, Phone_Number, SSN, Loan_Number)
VALUES(31,'Tallulah Dickson','321-5119 Proin Ave','39217','728-019-6086','791-35-5578','QV-207417');
INSERT INTO mortgage (id,Name,Street_Address, Zip_Code, Phone_Number, SSN, Loan_Number)
VALUES(32,'Alexis Owens','Ap #994-3254 Mauris Rd.','G0W 8M2','117-524-1252','973-54-2854','JG-181767');
INSERT INTO mortgage (id,Name,Street_Address, Zip_Code, Phone_Number, SSN, Loan_Number)
VALUES(33,'Macaulay Patrick','Ap #801-2682 Enim. Rd.','5401JL','332-319-0619','031-38-2377','BN-053999');
INSERT INTO mortgage (id,Name,Street_Address, Zip_Code, Phone_Number, SSN, Loan_Number)
VALUES(34,'Galena Lane','Ap #355-7062 Pellentesque Road','83819','028-791-7614','598-59-9303','FZ-483926');
INSERT INTO mortgage (id,Name,Street_Address, Zip_Code, Phone_Number, SSN, Loan_Number)
VALUES(35,'Nasim Barton','Ap #296-8283 Id St.','4009','998-642-2790','239-46-0516','QU-199754');
INSERT INTO mortgage (id,Name,Street_Address, Zip_Code, Phone_Number, SSN, Loan_Number)
VALUES(36,'Tamara Baldwin','Ap #465-9990 Sollicitudin Avenue','5734','936-291-5807','729-55-7738','OV-891406');
INSERT INTO mortgage (id,Name,Street_Address, Zip_Code, Phone_Number, SSN, Loan_Number)
VALUES(37,'Devin Underwood','Ap #795-5381 Morbi Street','RG1X 3ET','710-185-3690','669-03-8715','WC-529113');
INSERT INTO mortgage (id,Name,Street_Address, Zip_Code, Phone_Number, SSN, Loan_Number)
VALUES(38,'Kalia Washington','P.O. Box 511, 1273 Sed St.','41504','286-844-9835','388-80-5550','FU-283243');
INSERT INTO mortgage (id,Name,Street_Address, Zip_Code, Phone_Number, SSN, Loan_Number)
VALUES(39,'Blair Griffin','595-9633 Elementum, St.','M1P 8N7','830-277-2672','749-33-2840','GG-409918');
INSERT INTO mortgage (id,Name,Street_Address, Zip_Code, Phone_Number, SSN, Loan_Number)
VALUES(40,'Iliana Fowler','315-1593 Vitae Rd.','43972-000','530-582-0994','112-69-1920','NU-306540');
INSERT INTO mortgage (id,Name,Street_Address, Zip_Code, Phone_Number, SSN, Loan_Number)
VALUES(41,'Len Cardenas','Ap #302-2655 Aliquet St.','6513','459-928-5083','454-67-4996','XV-539883');
INSERT INTO mortgage (id,Name,Street_Address, Zip_Code, Phone_Number, SSN, Loan_Number)
VALUES(42,'Mary Maynard','Ap #959-4561 Donec Street','V8Z 1H5','544-203-3233','066-81-2748','IE-096452');
INSERT INTO mortgage (id,Name,Street_Address, Zip_Code, Phone_Number, SSN, Loan_Number)
VALUES(43,'Kyle Pierce','8147 Mauris Ave','87780','269-261-9606','052-58-0215','OH-215187');
INSERT INTO mortgage (id,Name,Street_Address, Zip_Code, Phone_Number, SSN, Loan_Number)
VALUES(44,'Jacqueline Cohen','8383 Velit. Ave','44954','293-943-5090','727-76-8997','RW-660647');
INSERT INTO mortgage (id,Name,Street_Address, Zip_Code, Phone_Number, SSN, Loan_Number)
VALUES(45,'Delilah Bean','3730 Elementum, Rd.','MD18 2KK','753-205-7978','979-87-4346','UF-921411');
INSERT INTO mortgage (id,Name,Street_Address, Zip_Code, Phone_Number, SSN, Loan_Number)
VALUES(46,'Blair Chandler','Ap #909-4588 Lorem St.','5284','567-359-1004','612-39-7145','RK-493713');
INSERT INTO mortgage (id,Name,Street_Address, Zip_Code, Phone_Number, SSN, Loan_Number)
VALUES(47,'Quinn Buckley','4871 Tellus St.','KG9T 5PD','264-941-2001','190-91-6013','CX-592169');
INSERT INTO mortgage (id,Name,Street_Address, Zip_Code, Phone_Number, SSN, Loan_Number)
VALUES(48,'Britanney Alvarez','P.O. Box 746, 7088 In Av.','4217','596-516-8707','310-91-9888','FQ-215461');
INSERT INTO mortgage (id,Name,Street_Address, Zip_Code, Phone_Number, SSN, Loan_Number)
VALUES(49,'Chava Collins','Ap #763-9987 Pede. Ave','95800','776-168-1328','879-80-7260','OE-276389');
INSERT INTO mortgage (id,Name,Street_Address, Zip_Code, Phone_Number, SSN, Loan_Number)
VALUES(50,'Rosalyn Goff','Ap #743-3921 Rutrum St.','0266NL','000-713-4183','612-83-0031','UU-487746');
INSERT INTO mortgage (id,Name,Street_Address, Zip_Code, Phone_Number, SSN, Loan_Number)
VALUES(51,'Constance Barron','5318 Cras St.','95699','904-228-4494','074-20-6581','EN-142061');
INSERT INTO mortgage (id,Name,Street_Address, Zip_Code, Phone_Number, SSN, Loan_Number)
VALUES(52,'Keane Wiley','P.O. Box 963, 3833 Gravida St.','50707','034-882-0272','700-18-8551','RU-532148');
INSERT INTO mortgage (id,Name,Street_Address, Zip_Code, Phone_Number, SSN, Loan_Number)
VALUES(53,'Evelyn Powell','351-4192 Quis, Road','R5L 7W8','827-230-3873','342-88-5840','SE-079946');
INSERT INTO mortgage (id,Name,Street_Address, Zip_Code, Phone_Number, SSN, Loan_Number)
VALUES(54,'Kalia Blake','110-1202 Amet Av.','2165','797-046-7158','583-87-9574','NF-332815');
INSERT INTO mortgage (id,Name,Street_Address, Zip_Code, Phone_Number, SSN, Loan_Number)
VALUES(55,'Chandler Joyner','P.O. Box 464, 8443 Imperdiet St.','61743','337-269-4787','426-57-2152','XP-503477');
INSERT INTO mortgage (id,Name,Street_Address, Zip_Code, Phone_Number, SSN, Loan_Number)
VALUES(56,'Gisela Weeks','1801 Vel Avenue','19774','371-297-0686','554-51-4507','QS-625912');
INSERT INTO mortgage (id,Name,Street_Address, Zip_Code, Phone_Number, SSN, Loan_Number)
VALUES(57,'Robert Lowe','Ap #368-5571 Luctus St.','71203','755-355-4601','659-19-1548','CU-227733');
INSERT INTO mortgage (id,Name,Street_Address, Zip_Code, Phone_Number, SSN, Loan_Number)
VALUES(58,'Phyllis Sanders','Ap #939-2471 Volutpat Road','11212','536-950-1707','631-70-5870','ZF-282234');
INSERT INTO mortgage (id,Name,Street_Address, Zip_Code, Phone_Number, SSN, Loan_Number)
VALUES(59,'Macy Gilbert','536-5328 In St.','9018HQ','923-902-6492','936-81-4207','TM-153436');
INSERT INTO mortgage (id,Name,Street_Address, Zip_Code, Phone_Number, SSN, Loan_Number)
VALUES(60,'Laurel Reyes','794-7726 Ac Ave','56751','474-142-3800','422-09-1025','XI-490689');
INSERT INTO mortgage (id,Name,Street_Address, Zip_Code, Phone_Number, SSN, Loan_Number)
VALUES(61,'Hayes Moran','9564 Dapibus Road','V4K 4T6','391-230-1729','499-99-6662','KQ-891202');
INSERT INTO mortgage (id,Name,Street_Address, Zip_Code, Phone_Number, SSN, Loan_Number)
VALUES(62,'Jolie Santana','P.O. Box 530, 7305 Lacus. St.','5563','636-850-4510','133-78-3312','PL-550772');
INSERT INTO mortgage (id,Name,Street_Address, Zip_Code, Phone_Number, SSN, Loan_Number)
VALUES(63,'Mechelle Luna','P.O. Box 768, 8686 Egestas. St.','23927','325-697-3644','055-16-4405','KY-289439');
INSERT INTO mortgage (id,Name,Street_Address, Zip_Code, Phone_Number, SSN, Loan_Number)
VALUES(64,'Christen Morrison','Ap #638-2175 Eleifend Rd.','27613','514-352-3883','092-00-9440','VU-887885');
INSERT INTO mortgage (id,Name,Street_Address, Zip_Code, Phone_Number, SSN, Loan_Number)
VALUES(65,'Chaim Merritt','Ap #846-8051 Phasellus Rd.','8291','259-646-8296','890-53-0245','EP-626599');
INSERT INTO mortgage (id,Name,Street_Address, Zip_Code, Phone_Number, SSN, Loan_Number)
VALUES(66,'Grant Miles','7094 In Ave','47263','947-102-5545','367-65-5114','UM-821025');
INSERT INTO mortgage (id,Name,Street_Address, Zip_Code, Phone_Number, SSN, Loan_Number)
VALUES(67,'Rhonda Spears','9160 Fermentum Ave','49345','018-140-0803','283-65-8646','JS-715563');
INSERT INTO mortgage (id,Name,Street_Address, Zip_Code, Phone_Number, SSN, Loan_Number)
VALUES(68,'Haviva Williamson','5458 Velit. Rd.','2221','953-525-1756','411-88-6044','RJ-408018');
INSERT INTO mortgage (id,Name,Street_Address, Zip_Code, Phone_Number, SSN, Loan_Number)
VALUES(69,'Nicholas Gregory','333-7926 Mattis. St.','7348','732-847-2301','682-12-0021','MR-541959');
INSERT INTO mortgage (id,Name,Street_Address, Zip_Code, Phone_Number, SSN, Loan_Number)
VALUES(70,'Lamar Sawyer','P.O. Box 901, 4875 Lobortis Av.','10407','162-915-8712','915-72-2114','HC-319748');
INSERT INTO mortgage (id,Name,Street_Address, Zip_Code, Phone_Number, SSN, Loan_Number)
VALUES(71,'Stephen Puckett','Ap #198-4700 Mauris Rd.','2970','835-092-1472','114-15-4000','YY-816621');
INSERT INTO mortgage (id,Name,Street_Address, Zip_Code, Phone_Number, SSN, Loan_Number)
VALUES(72,'Oren Ramsey','125-9221 Auctor, Street','98613','171-535-6031','351-56-9905','JJ-652795');
INSERT INTO mortgage (id,Name,Street_Address, Zip_Code, Phone_Number, SSN, Loan_Number)
VALUES(73,'Felix Miranda','5216 Ut Ave','52592','058-710-4289','863-02-7947','JD-022814');
INSERT INTO mortgage (id,Name,Street_Address, Zip_Code, Phone_Number, SSN, Loan_Number)
VALUES(74,'Maggie Mccormick','980-9354 Ac Av.','2261','087-727-1107','525-56-4701','RY-326757');
INSERT INTO mortgage (id,Name,Street_Address, Zip_Code, Phone_Number, SSN, Loan_Number)
VALUES(75,'Dale Randall','Ap #675-2072 Lectus. Rd.','69853','944-873-7994','508-66-5028','GK-975190');
INSERT INTO mortgage (id,Name,Street_Address, Zip_Code, Phone_Number, SSN, Loan_Number)
VALUES(76,'Cameran Taylor','630-7757 Velit. Ave','6168','940-864-3046','677-31-8913','BU-419938');
INSERT INTO mortgage (id,Name,Street_Address, Zip_Code, Phone_Number, SSN, Loan_Number)
VALUES(77,'August Shaw','8843 Tortor Ave','37654','824-206-9672','638-48-0110','NO-490050');
INSERT INTO mortgage (id,Name,Street_Address, Zip_Code, Phone_Number, SSN, Loan_Number)
VALUES(78,'Dai Holder','P.O. Box 716, 5792 Vitae, Ave','4976','056-462-5578','755-05-2044','RB-261681');
INSERT INTO mortgage (id,Name,Street_Address, Zip_Code, Phone_Number, SSN, Loan_Number)
VALUES(79,'Damon Graham','Ap #243-6569 Ornare Avenue','5434','990-266-3511','355-38-7748','RN-319029');
INSERT INTO mortgage (id,Name,Street_Address, Zip_Code, Phone_Number, SSN, Loan_Number)
VALUES(80,'Adara Carey','Ap #640-5801 Eget, Road','6732','671-979-3175','770-36-2508','IZ-471949');
INSERT INTO mortgage (id,Name,Street_Address, Zip_Code, Phone_Number, SSN, Loan_Number)
VALUES(81,'Jorden Cline','1893 Venenatis Ave','77192','009-579-7196','871-47-5159','OU-468491');
INSERT INTO mortgage (id,Name,Street_Address, Zip_Code, Phone_Number, SSN, Loan_Number)
VALUES(82,'Raven Baxter','703-3100 Non Rd.','2996','122-376-5180','522-43-2267','SH-127818');
INSERT INTO mortgage (id,Name,Street_Address, Zip_Code, Phone_Number, SSN, Loan_Number)
VALUES(83,'Honorato Mayer','P.O. Box 984, 2514 Mauris Road','4528','010-877-4514','163-91-9997','NR-626236');
INSERT INTO mortgage (id,Name,Street_Address, Zip_Code, Phone_Number, SSN, Loan_Number)
VALUES(84,'Fulton Doyle','7542 Ornare, Rd.','2856','214-051-7119','311-68-8723','NO-365711');
INSERT INTO mortgage (id,Name,Street_Address, Zip_Code, Phone_Number, SSN, Loan_Number)
VALUES(85,'Dante Rosa','P.O. Box 323, 2638 Parturient St.','39895','505-390-1004','306-28-6253','HD-889497');
INSERT INTO mortgage (id,Name,Street_Address, Zip_Code, Phone_Number, SSN, Loan_Number)
VALUES(86,'Kimberly Kelly','P.O. Box 860, 536 Ultrices St.','15074','826-547-1703','943-47-2370','SX-538830');
INSERT INTO mortgage (id,Name,Street_Address, Zip_Code, Phone_Number, SSN, Loan_Number)
VALUES(87,'Mikayla Mcdaniel','P.O. Box 209, 4103 Sollicitudin St.','75939-723','943-758-7524','918-00-5632','WJ-928336');
INSERT INTO mortgage (id,Name,Street_Address, Zip_Code, Phone_Number, SSN, Loan_Number)
VALUES(88,'Brynne Hartman','Ap #561-2954 Ut, Street','EJ4 5SV','155-138-2239','093-92-9067','AN-348990');
INSERT INTO mortgage (id,Name,Street_Address, Zip_Code, Phone_Number, SSN, Loan_Number)
VALUES(89,'Anthony Knox','P.O. Box 338, 6607 Vel, Ave','60278-871','992-922-6128','965-06-9929','TJ-469774');
INSERT INTO mortgage (id,Name,Street_Address, Zip_Code, Phone_Number, SSN, Loan_Number)
VALUES(90,'Ira Luna','Ap #579-5351 Semper, St.','2267','891-777-8825','326-75-8393','HS-208500');
INSERT INTO mortgage (id,Name,Street_Address, Zip_Code, Phone_Number, SSN, Loan_Number)
VALUES(91,'Brett Cervantes','8532 Magna Av.','7440','636-586-1645','272-39-2450','NL-101722');
INSERT INTO mortgage (id,Name,Street_Address, Zip_Code, Phone_Number, SSN, Loan_Number)
VALUES(92,'Jameson Moss','668-7549 Felis St.','4754','705-558-7747','345-70-7831','ZP-222441');
INSERT INTO mortgage (id,Name,Street_Address, Zip_Code, Phone_Number, SSN, Loan_Number)
VALUES(93,'Dillon Carey','5424 Posuere Ave','U3 5YJ','851-301-6525','592-72-1962','AZ-006160');
INSERT INTO mortgage (id,Name,Street_Address, Zip_Code, Phone_Number, SSN, Loan_Number)
VALUES(94,'Quinlan Mosley','Ap #828-1971 A Av.','11208','538-293-4757','284-61-4589','WH-358670');
INSERT INTO mortgage (id,Name,Street_Address, Zip_Code, Phone_Number, SSN, Loan_Number)
VALUES(95,'Jonas Benjamin','3493 Adipiscing Ave','8109','997-191-9562','299-62-4244','KC-090841');
INSERT INTO mortgage (id,Name,Street_Address, Zip_Code, Phone_Number, SSN, Loan_Number)
VALUES(96,'Kato Lucas','P.O. Box 601, 7176 Vulputate Avenue','14069','309-695-6031','038-87-0829','UE-576392');
INSERT INTO mortgage (id,Name,Street_Address, Zip_Code, Phone_Number, SSN, Loan_Number)
VALUES(97,'Chester Branch','508-9039 Auctor, St.','21768','842-709-0213','499-36-3905','CX-691818');
INSERT INTO mortgage (id,Name,Street_Address, Zip_Code, Phone_Number, SSN, Loan_Number)
VALUES(98,'Ahmed Simon','1554 Purus Ave','E6P 3R8','865-022-1523','892-22-9304','KX-656650');
INSERT INTO mortgage (id,Name,Street_Address, Zip_Code, Phone_Number, SSN, Loan_Number)
VALUES(99,'Fay Hardin','3221 Eros. Ave','28036-313','323-871-5674','556-31-0948','TK-993111');
INSERT INTO mortgage (id,Name,Street_Address, Zip_Code, Phone_Number, SSN, Loan_Number)
VALUES(100,'Yoshio Leach','Ap #159-3100 Nam Street','74178','236-767-3427','380-35-4785','GB-249746');









/*Update queries*/
update plugin set plugin_name='KEY_VALUE_EXTRACTION' where plugin_name='REGEX_EXTRACTION';
update kv_page_process set page_level_field_name=(select plugin_config_value from batch_class_plugin_config where batch_class_plugin_config.id=kv_page_process.batch_class_plugin_config_id);
update plugin set plugin_desc = 'Key Value Extraction Plugin' where plugin_name = 'KEY_VALUE_EXTRACTION';
update plugin_config set config_desc = 'Recostar Extraction color switch' where config_name = 'recostar_extraction.color_switch';
update plugin_config set config_desc = 'Recostar Extraction Switch' where config_name = 'recostar_extraction.switch';
update plugin_config set is_mandatory=1 where is_mandatory is null ;
update plugin_config set config_multivalue = 1 where config_name = 'lucene.valid_extensions';


/*Insert query for inserting property in CreateMultipageFile Plugin*/
INSERT INTO plugin_config (creation_date, last_modified, config_datatype, config_desc, is_mandatory, config_multivalue, config_name, order_number, plugin_id) VALUES (now(),now(), 'STRING', 'Multipage File Creation Type', b'1', b'0', 'createMultipageTif.multipage_file_type', NULL, (select id from plugin where plugin_name='CREATEMULTIPAGE_FILES'));

/*Inserting default data for createMultipageTif.multipage_file_type property*/
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'PDF',(select id from plugin_config where config_name='createMultipageTif.multipage_file_type'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'Tiff',(select id from plugin_config where config_name='createMultipageTif.multipage_file_type'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'Tiff and PDF',(select id from plugin_config where config_name='createMultipageTif.multipage_file_type'));

	

/*Insert query for inserting two properties in CopyMultipageFile Plugin*/
INSERT INTO plugin_config (creation_date, last_modified, config_datatype, config_desc, is_mandatory, config_multivalue, config_name, order_number, plugin_id) VALUES (now(),now(), 'STRING', 'Copy Multipage File Type', b'1', b'0', 'batch.copy_file_type', NULL, (select id from plugin where plugin_name='COPY_BATCH_XML'));

/*Inserting default data for batch.copy_file_type*/
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'PDF',(select id from plugin_config where config_name='batch.copy_file_type'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'Tiff',(select id from plugin_config where config_name='batch.copy_file_type'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'Tiff and PDF',(select id from plugin_config where config_name='batch.copy_file_type'));

delete from batch_class_plugin_config where plugin_config_id in (select id from plugin_config where plugin_config.config_name='createMultipageTif.switch');
delete from plugin_config_sample_value where plugin_config_id in (select id from plugin_config where plugin_config.config_name='createMultipageTif.switch');

/*Delete queries for plugin config updates*/
delete from plugin_config where plugin_config.config_name='createMultipageTif.switch';

/*Insert query for inserting da.delete_document_first_page_switch property in Document Assemble Plugin*/

INSERT INTO plugin_config(creation_date,last_modified,config_datatype,config_desc,config_multivalue,config_name,plugin_id) VALUES (now(),now(),'STRING','DA Delete Document First Page Switch',0,'da.delete_document_first_page_switch',(select id from plugin where plugin_name='DOCUMENT_ASSEMBLER'));

INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'OFF',(select id from plugin_config where config_name='da.delete_document_first_page_switch'));

INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'ON',(select id from plugin_config where config_name='da.delete_document_first_page_switch'));

/*Insert query for inserting itext.searchable_pdf_type in CreateMultiPage Files Plugin*/

INSERT INTO plugin_config(creation_date,last_modified,config_datatype,config_desc,config_multivalue,config_name,plugin_id) VALUES (now(),now(),'STRING','IText Searchable PDF Type',0,'itext.searchable_pdf_type',(select id from plugin where plugin_name='CREATEMULTIPAGE_FILES'));

INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'PDF',(select id from plugin_config where config_name='itext.searchable_pdf_type'));

INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'PDF Advanced',(select id from plugin_config where config_name='itext.searchable_pdf_type'));

/*Query inserted to run during updation if the value of dbExport.UserName property has 'e' small. Property has to be changed only for Mysql*/
Update plugin_config set config_name='dbExport.database.userName'  where config_name = 'dbexport.database.userName';

/*Patch changes for v.3.1 release*/

/*DA properties*/
INSERT INTO plugin_config(creation_date,last_modified,config_datatype,config_desc, is_mandatory,config_multivalue,config_name,plugin_id) VALUES (now(),now(),'STRING','Advanced DA Switch',1,0,'da.da_advanced_algorithm',(select id from plugin where plugin_name='DOCUMENT_ASSEMBLER'));
INSERT INTO plugin_config(creation_date,last_modified,config_datatype,config_desc, is_mandatory,config_multivalue,config_name,plugin_id) VALUES (now(),now(),'INTEGER','DA First Page Confidence Threshold',1,0,'da.first_page_confidence_threshold',(select id from plugin where plugin_name='DOCUMENT_ASSEMBLER'));
INSERT INTO plugin_config(creation_date,last_modified,config_datatype,config_desc, is_mandatory,config_multivalue,config_name,plugin_id) VALUES (now(),now(),'INTEGER','DA Middle Page Confidence Threshold',1,0,'da.middle_page_confidence_threshold',(select id from plugin where plugin_name='DOCUMENT_ASSEMBLER'));
INSERT INTO plugin_config(creation_date,last_modified,config_datatype,config_desc, is_mandatory,config_multivalue,config_name,plugin_id) VALUES (now(),now(),'INTEGER','DA Last Page Confidence Threshold',1,0,'da.last_page_confidence_threshold',(select id from plugin where plugin_name='DOCUMENT_ASSEMBLER'));
INSERT INTO plugin_config(creation_date,last_modified,config_datatype,config_desc, is_mandatory,config_multivalue,config_name,plugin_id) VALUES (now(),now(),'INTEGER','Predefined Document Confidence Threshold',1,0,'da.predefined_document_confidence_threshold',(select id from plugin where plugin_name='DOCUMENT_ASSEMBLER'));
INSERT INTO plugin_config(creation_date,last_modified,config_datatype,config_desc, is_mandatory,config_multivalue,config_name,plugin_id) VALUES (now(),now(),'STRING','Predefined Document Type',0,0,'da.predefined_document_type',(select id from plugin where plugin_name='DOCUMENT_ASSEMBLER'));
INSERT INTO plugin_config(creation_date,last_modified,config_datatype,config_desc, is_mandatory,config_multivalue,config_name,plugin_id) VALUES (now(),now(),'STRING','Change Unknown Document Type Switch',1,0,'da.switch_unknown_predefined_document_type',(select id from plugin where plugin_name='DOCUMENT_ASSEMBLER'));
INSERT INTO plugin_config(creation_date,last_modified,config_datatype,config_desc, is_mandatory,config_multivalue,config_name,plugin_id) VALUES (now(),now(),'STRING','Change Unknown Document To Document Type',0,0,'da.unknown_predefined_document_type',(select id from plugin where plugin_name='DOCUMENT_ASSEMBLER'));

/*Insert into plugin config sample values.*/
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'OFF',(select id from plugin_config where config_name='da.da_advanced_algorithm'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'OFF',(select id from plugin_config where config_name='da.switch_unknown_predefined_document_type'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'ON',(select id from plugin_config where config_name='da.switch_unknown_predefined_document_type'));

/*Query added to increase the column size of Regex pattern from 255 to 700*/
ALTER table batch_class_field MODIFY COLUMN validation_pattern VARCHAR(700);
ALTER table field_type MODIFY COLUMN pattern VARCHAR(700);
ALTER table kv_extraction MODIFY COLUMN key_pattern VARCHAR(700);
ALTER table kv_extraction MODIFY COLUMN value_pattern VARCHAR(700);
ALTER table kv_page_process MODIFY COLUMN key_pattern VARCHAR(700);
ALTER table kv_page_process MODIFY COLUMN value_pattern VARCHAR(700);
ALTER table regex_validation MODIFY COLUMN pattern VARCHAR(700);
ALTER table regex_pattern MODIFY COLUMN pattern VARCHAR(700);
ALTER table table_columns_info MODIFY COLUMN validation_pattern VARCHAR(700);
ALTER table table_columns_info MODIFY COLUMN column_pattern VARCHAR(700);
ALTER table table_columns_info MODIFY COLUMN column_header_pattern VARCHAR(700);
ALTER table table_columns_info MODIFY COLUMN between_left VARCHAR(700);
ALTER table table_columns_info MODIFY COLUMN between_right VARCHAR(700);
ALTER table table_info MODIFY COLUMN start_pattern VARCHAR(700);
ALTER table table_info MODIFY COLUMN end_pattern VARCHAR(700);

/*Web Scanner “Multifeed Detection” feature*/ 
INSERT INTO scanner_master_configuration(creation_date, last_modified, description, is_mandatory, config_multivalue, config_name, config_data_type) VALUES (now(), now(), 'Enable Multifeed Error Detection', 1, 1, 'multifeed_detection', 'Boolean');
INSERT INTO scanner_master_config_sample_value (creation_date, last_modified, sample_value, master_config_id) VALUES (now(), now(), 'true', (select id from scanner_master_configuration where config_name='multifeed_detection'));
INSERT INTO scanner_master_config_sample_value (creation_date, last_modified, sample_value, master_config_id) VALUES (now(), now(),'false', (select id from scanner_master_configuration where config_name='multifeed_detection')); 

/*Password Masking and Encryption*/
update plugin_config set plugin_config.config_datatype = 'PASSWORD' where plugin_config.config_name in ('fuzzydb.database.password','cmis.server_password','filebound.password','dbExport.database.password');


update table_info set no_of_rows = 5 where no_of_rows is null;

INSERT INTO plugin_config (creation_date,last_modified,config_datatype,config_desc,is_mandatory,config_multivalue,config_name,order_number,plugin_id) VALUES (now(),now(),'STRING','Index Field Value Separator',0,0,'validation.index_field_value_separator',NULL,(select id from plugin where plugin_desc = 'Validate Document Plugin'));
INSERT INTO plugin_config (creation_date,last_modified,config_datatype,config_desc,is_mandatory,config_multivalue,config_name,order_number,plugin_id) VALUES (now(),now(),'STRING','Insert Table Row Script Switch',1,0,'validation.insert_table_row_script_switch',NULL,(select id from plugin where plugin_desc = 'Validate Document Plugin'));

INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'OFF',(select id from plugin_config where config_name='validation.insert_table_row_script_switch'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'ON',(select id from plugin_config where config_name='validation.insert_table_row_script_switch'));


/*Export to HPII Filenet plugin in db*/
INSERT INTO plugin(creation_date,last_modified,plugin_name,plugin_desc,plugin_version,workflow_name) VALUES (now(),now(),'EXPORT_TO_HPII_FILENET','Export To HPII Filenet plugin','1.0.0.0','Export_To_HPII_Filenet_Plugin');

INSERT INTO plugin_config(creation_date,last_modified,config_datatype,config_desc,config_multivalue,config_name,plugin_id) VALUES (now(),now(),'STRING','Export To HPII Filenet Config file path',0,'exporttoHPIIfilenet.config_file_path',(select id from plugin where plugin_name='EXPORT_TO_HPII_FILENET'));
INSERT INTO plugin_config(creation_date,last_modified,config_datatype,config_desc,config_multivalue,config_name,plugin_id) VALUES (now(),now(),'STRING','Export To HPII Filenet Transaction Folder Path',0,'exporttoHPIIfilenet.transaction_folder_path',(select id from plugin where plugin_name='EXPORT_TO_HPII_FILENET'));
INSERT INTO plugin_config(creation_date,last_modified,config_datatype,config_desc,config_multivalue,config_name,plugin_id) VALUES (now(),now(),'STRING','Export To HPII Filenet Upload File Extension',0,'exporttoHPIIfilenet.upload_file_type_ext',(select id from plugin where plugin_name='EXPORT_TO_HPII_FILENET'));
INSERT INTO plugin_config(creation_date,last_modified,config_datatype,config_desc,config_multivalue,config_name,plugin_id) VALUES (now(),now(),'STRING','Export To HPII Filenet Server Switch ON/OFF',0,'exporttoHPIIfilenet.switch',(select id from plugin where plugin_name='EXPORT_TO_HPII_FILENET'));
INSERT INTO plugin_config(creation_date,last_modified,config_datatype,config_desc,config_multivalue,config_name,plugin_id) VALUES (now(),now(),'STRING','Export To HPII Filenet File Name',0,'exporttoHPIIfilenet.file_name',(select id from plugin where plugin_name='EXPORT_TO_HPII_FILENET'));

INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'tif',(select id from plugin_config where config_name='exporttoHPIIfilenet.upload_file_type_ext'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'pdf',(select id from plugin_config where config_name='exporttoHPIIfilenet.upload_file_type_ext'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'OFF',(select id from plugin_config where config_name='exporttoHPIIfilenet.switch'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'ON',(select id from plugin_config where config_name='exporttoHPIIfilenet.switch'));

/*CMIS Export plugin*/
INSERT INTO plugin_config(creation_date,last_modified,config_datatype,config_desc,is_mandatory,config_multivalue,config_name,plugin_id) VALUES (now(),now(),'STRING','CMIS Export File Name',0,0,'cmis.file_name',(select id from plugin where plugin_name='CMIS_EXPORT'));
INSERT INTO plugin_config(creation_date,last_modified,config_datatype,config_desc,is_mandatory,config_multivalue,config_name,plugin_id) VALUES (now(),now(),'STRING','CMIS Export Client Key',0,0,'cmis.client_key',(select id from plugin where plugin_name='CMIS_EXPORT'));
INSERT INTO plugin_config(creation_date,last_modified,config_datatype,config_desc,is_mandatory,config_multivalue,config_name,plugin_id) VALUES (now(),now(),'STRING','CMIS Export Secret Key',0,0,'cmis.secret_key',(select id from plugin where plugin_name='CMIS_EXPORT'));
INSERT INTO plugin_config(creation_date,last_modified,config_datatype,config_desc,is_mandatory,config_multivalue,config_name,plugin_id) VALUES (now(),now(),'STRING','CMIS Export Refresh Token',0,0,'cmis.refresh_token',(select id from plugin where plugin_name='CMIS_EXPORT'));
INSERT INTO plugin_config(creation_date,last_modified,config_datatype,config_desc,is_mandatory,config_multivalue,config_name,plugin_id) VALUES (now(),now(),'STRING','CMIS Export Redirect URL',0,0,'cmis.redirect_url',(select id from plugin where plugin_name='CMIS_EXPORT'));
INSERT INTO plugin_config(creation_date,last_modified,config_datatype,config_desc,is_mandatory,config_multivalue,config_name,plugin_id) VALUES (now(),now(),'STRING','CMIS Export Network',0,0,'cmis.network',(select id from plugin where plugin_name='CMIS_EXPORT'));



/*In case of upgradation to change the name of switch.*/
update plugin_config set config_desc = "Create Compare Thumbnail Switch" where config_desc = "Create thumbnails switch";
update plugin_config set config_name = 'dbExport.database.userName' where config_name = 'dbexport.database.userName';

/*Insert query for inserting two properties in KEY_VALUE_LEARNING_PLUGIN Plugin*/
INSERT INTO plugin_config (creation_date, last_modified, config_datatype, config_desc, is_mandatory, config_multivalue, config_name, order_number, plugin_id) VALUES (now(),now(), 'STRING', 'Numeric Key Learning Switch', b'1', b'0', 'keyValueLearning.numerickeyswitch', NULL, (select id from plugin where plugin_name='KEY_VALUE_LEARNING_PLUGIN'));

/*Inserting default data for KEY_VALUE_LEARNING_PLUGIN*/
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'OFF',(select id from plugin_config where config_name='keyValueLearning.numerickeyswitch'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'ON',(select id from plugin_config where config_name='keyValueLearning.numerickeyswitch'));

/*Setting the weight value of kv extraction to 1 where it is null.*/
update kv_extraction set weight = 1 where weight is null;

/*Adding a property of DA plugin for barcode classification.*/
INSERT INTO plugin_config(creation_date,last_modified,config_datatype,config_desc, is_mandatory,config_multivalue,config_name,plugin_id) VALUES (now(),now(),'STRING','Regex Classification Switch',1,0,'da.switch_regex_classification',(select id from plugin where plugin_name='DOCUMENT_ASSEMBLER'));
INSERT INTO plugin_config(creation_date,last_modified,config_datatype,config_desc, is_mandatory,config_multivalue,config_name,plugin_id) VALUES (now(),now(),'STRING','Regex Classification Pattern',0,0,'da.regex_classification_pattern',(select id from plugin where plugin_name='DOCUMENT_ASSEMBLER'));
INSERT INTO plugin_config(creation_date,last_modified,config_datatype,config_desc, is_mandatory,config_multivalue,config_name,plugin_id) VALUES (now(),now(),'STRING','Default Document Type',0,0,'da.document_type_regex_classification',(select id from plugin where plugin_name='DOCUMENT_ASSEMBLER'));

/*Adding plugin property config sample values */
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'OFF',(select id from plugin_config where config_name='da.switch_regex_classification'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'ON',(select id from plugin_config where config_name='da.switch_regex_classification'));

/*Updating the value of property if it is empty.*/
update batch_class_plugin_config set batch_class_plugin_config.plugin_config_value = '-limit area 100mb'
where batch_class_plugin_config.plugin_config_id in (select plugin_config.id from plugin_config where plugin_config.config_name='imagemagick.open_input_image_parameters') 
and batch_class_plugin_config.batch_class_plugin_id in (select batch_class_plugin.id from batch_class_plugin where batch_class_plugin.batch_class_module_id in (select batch_class_module.id from batch_class_module where batch_class_module.workflow_name = 'Folder_Import_Module' and batch_class_module.batch_class_id in (
select batch_class.id from batch_class where batch_class.batch_class_name in ('MailroomAutomationTemplate', 'SearchablePDFTemplate', 'GridComputingTemplate'))) and batch_class_plugin.plugin_id in (
select plugin.id from plugin where plugin.plugin_name='IMPORT_MULTIPAGE_FILES')) and batch_class_plugin_config.plugin_config_value NOT LIKE '%_%';

/*Deleting the property in case of upgrades.*/
DELETE from plugin_config_sample_value where sample_value = 'com.microsoft.jdbc.sqlserver.SQLServerDriver' and plugin_config_id = (select id from plugin_config where config_name='fuzzydb.database.driver');

/*Adding a property of Validation plugin.*/
INSERT INTO plugin_config(creation_date,last_modified,config_datatype,config_desc, is_mandatory,config_multivalue,config_name,plugin_id) VALUES (now(),now(),'STRING','Sticky Index Field Switch',1,0,'validation.sticky_index_field_switch',(select id from plugin where plugin_name='VALIDATE_DOCUMENT'));

/*Adding plugin property config sample values */
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'OFF',(select id from plugin_config where config_name='validation.sticky_index_field_switch'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'ON',(select id from plugin_config where config_name='validation.sticky_index_field_switch'));

/*Adding a property of Validation plugin.*/
INSERT INTO plugin_config(creation_date,last_modified,config_datatype,config_desc, is_mandatory,config_multivalue,config_name,plugin_id) VALUES (now(),now(),'STRING','Table Extraction Suggestion Box Switch',1,0,'validation.table_extraction_suggestion_box_switch',(select id from plugin where plugin_name='VALIDATE_DOCUMENT'));

/*Adding plugin property config sample values */
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'ON',(select id from plugin_config where config_name='validation.table_extraction_suggestion_box_switch'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'OFF',(select id from plugin_config where config_name='validation.table_extraction_suggestion_box_switch'));

/*Updating the table rule operator column setting value to OR if null*/
update table_info set table_rule_operator = 'OR' where table_rule_operator is null;

/*Adding a property of recostar hocr plugin.*/
INSERT INTO plugin_config(creation_date,last_modified,config_datatype,config_desc, is_mandatory,config_multivalue,config_name,plugin_id) VALUES (now(),now(),'STRING','Recostar Deskew Switch',1,0,'recostar.deskew_switch',(select id from plugin where plugin_name='RECOSTAR_HOCR'));

/*Adding plugin property config sample values */
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'OFF',(select id from plugin_config where config_name='recostar.deskew_switch'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'ON',(select id from plugin_config where config_name='recostar.deskew_switch'));

/* Adding Nuance plugin */
INSERT INTO plugin(creation_date,last_modified,plugin_name,plugin_desc,plugin_version,workflow_name) VALUES (now(),now(),'NUANCE_HOCR','Nuance Plugin','1.0.0.0','Nuance_OCR_Plugin');

/* Adding Nuance plugin config values  */
INSERT INTO plugin_config(creation_date,last_modified,config_datatype,config_desc, is_mandatory,config_multivalue,config_name,plugin_id) VALUES (now(),now(),'STRING','Nuance Switch',1,0,'nuance.switch',(select id from plugin where plugin_name='NUANCE_HOCR'));
INSERT INTO plugin_config(creation_date,last_modified,config_datatype,config_desc, is_mandatory,config_multivalue,config_name,plugin_id) VALUES (now(),now(),'STRING','Nuance Auto Rotate/Deskew Switch',1,0,'nuance.auto_rotate_switch',(select id from plugin where plugin_name='NUANCE_HOCR'));
INSERT INTO plugin_config(creation_date,last_modified,config_datatype,config_desc, is_mandatory,config_multivalue,config_name,plugin_id) VALUES (now(),now(),'STRING','Nuance Valid Extensions',1,0,'nuance.valid_extensions',(select id from plugin where plugin_name='NUANCE_HOCR'));

/*   Adding Nuance plugin config sample valuess   */
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'ON',(select id from plugin_config where config_name='nuance.switch'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'OFF',(select id from plugin_config where config_name='nuance.switch'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'OFF',(select id from plugin_config where config_name='nuance.auto_rotate_switch'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'ON',(select id from plugin_config where config_name='nuance.auto_rotate_switch'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'tif',(select id from plugin_config where config_name='nuance.valid_extensions'));

/* Adding Nuance extraction plugin */
INSERT INTO plugin(creation_date,last_modified,plugin_name,plugin_desc,plugin_version,workflow_name) VALUES (now(),now(),'NUANCE_EXTRACTION_PLUGIN','Nuance Extraction Plugin','1.0.0.0','Nuance_Doc_Fields_Extraction_Plugin');

/* Adding Nuance extraction plugin config values  */
INSERT INTO plugin_config(creation_date,last_modified,config_datatype,config_desc, is_mandatory,config_multivalue,config_name,plugin_id) VALUES (now(),now(),'STRING','Nuance Auto Rotate/Deskew Switch',1,0,'nuance_extraction.auto_rotate_deskew_switch',(select id from plugin where plugin_name='NUANCE_EXTRACTION_PLUGIN'));
INSERT INTO plugin_config(creation_date,last_modified,config_datatype,config_desc, is_mandatory,config_multivalue,config_name,plugin_id) VALUES (now(),now(),'STRING','Nuance Extraction Switch',1,0,'nuance_extraction.switch',(select id from plugin where plugin_name='NUANCE_EXTRACTION_PLUGIN'));


/*   Adding Nuance extraction plugin config sample valuess */
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'OFF',(select id from plugin_config where config_name='nuance_extraction.auto_rotate_deskew_switch'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'ON',(select id from plugin_config where config_name='nuance_extraction.auto_rotate_deskew_switch'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'ON',(select id from plugin_config where config_name='nuance_extraction.switch'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'OFF',(select id from plugin_config where config_name='nuance_extraction.switch'));


/*Changes for server_registry table for failOver mechanism*/
update server_registry set is_license_server = 0 where is_license_server is null;

/* inserting driver name for ms always on feature */
DELETE from plugin_config_sample_value where sample_value = 'com.microsoft.jdbc.sqlserver.SQLServerDriver' and plugin_config_id = (select id from plugin_config where config_name='dbExport.database.driver');
INSERT INTO plugin_config_sample_value(creation_date, last_modified, sample_value, plugin_config_id) VALUES ( now(), now(), 'com.microsoft.sqlserver.jdbc.SQLServerDriver', (select id from plugin_config where config_name = 'dbExport.database.driver'));
INSERT INTO plugin_config_sample_value(creation_date, last_modified, sample_value, plugin_config_id) VALUES ( now(), now(), 'com.microsoft.sqlserver.jdbc.SQLServerDriver', (select id from plugin_config where config_name = 'fuzzydb.database.driver'));



/*updating plugin id to plugin configs where id was null*/
update plugin_config set plugin_id = (select id from plugin where plugin_name='DOCUMENT_ASSEMBLER') where config_name = 'da.barcode_confidence';
update plugin_config set plugin_id = (select id from plugin where plugin_name='DOCUMENT_ASSEMBLER') where config_name = 'da.rule_fp_mp_lp';
update plugin_config set plugin_id = (select id from plugin where plugin_name='DOCUMENT_ASSEMBLER') where config_name = 'da.rule_fp';
update plugin_config set plugin_id = (select id from plugin where plugin_name='DOCUMENT_ASSEMBLER') where config_name = 'da.rule_mp';
update plugin_config set plugin_id = (select id from plugin where plugin_name='DOCUMENT_ASSEMBLER') where config_name = 'da.rule_lp';
update plugin_config set plugin_id = (select id from plugin where plugin_name='DOCUMENT_ASSEMBLER') where config_name = 'da.rule_fp_lp';
update plugin_config set plugin_id = (select id from plugin where plugin_name='DOCUMENT_ASSEMBLER') where config_name = 'da.rule_fp_mp';
update plugin_config set plugin_id = (select id from plugin where plugin_name='DOCUMENT_ASSEMBLER') where config_name = 'da.rule_mp_lp';
update plugin_config set plugin_id = (select id from plugin where plugin_name='DOCUMENT_ASSEMBLER') where config_name = 'da.factory_classification';

/*regex pattern update*/
update regex_pattern set pattern='\\d{2}/\\d{2}/\\d{4}' where pattern='\\d{1,2}/\\d{1,2}/\\d{1,4}';
update regex_pattern set pattern='(?i:((Jan)(uary)?|(Feb)(ruary)?|Mar(ch)?|April|May|June|July|Aug(ust)?|(Sept|Nov|Dec)(ember)?|oct(ober)?))((-|\\s)\\d{2,4}\\,?\\s?\\d{4})|\\d{2} (?i:((Jan)(uary)?|(Feb)(ruary)?|Mar(ch)?|April|May|June|July|Aug(ust)?|(Sept|Nov|Dec)(ember)?|oct(ober)?)) \\d{4}' where pattern='(?i:((Jan|Feb)(ruary)?|Mar(ch)?|April|May|June|July|Aug(ust)?|(Sept|Nov|Nov|Dec)(ember)?|oct(ober)?))((-|\\s)\\d{2,4}\\,?\\s?\\d{4})|\\d{2} (?i:((Jan|Feb)(ruary)?|Mar(ch)?|April|May|June|July|Aug(ust)?|(Sept|Nov|Nov|Dec)(ember)?|oct(ober)?)) \\d{4}';
update regex_pattern set pattern='^([a-zA-Z0-9_.-]+)@([A-Za-z0-9.-]+)\\.([a-zA-Z]{2,6})$' where pattern='/^([a-z0-9_.-]+)@([da-z.-]+).([a-z.]{2,6})$/';
update regex_pattern set pattern='(?i:((EFX\\sFACTA)|Equifax(\\/Facta)?)|(\\s)?(Beacon)\\s?5?(\\.0)?)' where pattern='(?i:((EFX\\sFACTA)|Equifax(\\/Facta)?)?(\\s)?(Beacon)\\s?5?(\\.0)?)';

/*Scanner property addition*/
INSERT INTO scanner_master_configuration(creation_date, last_modified, description, is_mandatory, config_multivalue, config_name, config_data_type) VALUES (now(), now(), 'Enable Rescan', 1, 1, 'rescan', 'Boolean');
INSERT INTO scanner_master_configuration(creation_date, last_modified, description, is_mandatory, config_multivalue, config_name, config_data_type) VALUES (now(), now(), 'Enable Delete', 1, 1, 'delete', 'Boolean');

INSERT INTO scanner_master_config_sample_value (creation_date, last_modified, sample_value, master_config_id) VALUES (now(), now(), 'true', (select id from scanner_master_configuration where config_name='rescan')); 
INSERT INTO scanner_master_config_sample_value (creation_date, last_modified, sample_value, master_config_id) VALUES (now(), now(),'false', (select id from scanner_master_configuration where config_name='rescan'));
INSERT INTO scanner_master_config_sample_value (creation_date, last_modified, sample_value, master_config_id) VALUES (now(), now(), 'true', (select id from scanner_master_configuration where config_name='delete')); 
INSERT INTO scanner_master_config_sample_value (creation_date, last_modified, sample_value, master_config_id) VALUES (now(), now(),'false', (select id from scanner_master_configuration where config_name='delete'));

/*For export batch final folder two properties added in plugin_config.*/
INSERT INTO plugin_config (creation_date, last_modified, config_datatype, config_desc, config_multivalue, config_name, plugin_id) VALUES (now(), now(), 'STRING', 'Multipage File Export Folder', b'0', 'batch.multipage_export_folder_path', (Select id from plugin where plugin_name = 'COPY_BATCH_XML'));

INSERT INTO plugin_config (creation_date, last_modified, config_datatype, config_desc, config_multivalue, config_name, plugin_id) VALUES (now(), now(), 'STRING', 'Multipage File Name', b'0', 'batch.multipage_export_file_name', (Select id from plugin where plugin_name = 'COPY_BATCH_XML'));

/* Creating table db_export */
CREATE TABLE db_export (id bigint(20) NOT NULL AUTO_INCREMENT, invoice_date varchar(255) DEFAULT NULL,part_number varchar(255) DEFAULT NULL,invoice_total varchar(255) DEFAULT NULL,state varchar(255) DEFAULT NULL,city varchar(255) DEFAULT NULL, PRIMARY KEY (id));

/*default value for folder moniter service */
update server_registry set is_folderMoniter_server = 0 where is_folderMoniter_server is null;

/*default value for execution capacity on server registry. */
update server_registry set execution_capacity = 0 where execution_capacity is null;

/*adding new property for document assembler file boundary classification*/
INSERT INTO plugin_config(creation_date,last_modified,config_datatype,config_desc,config_multivalue,config_name,plugin_id) VALUES (now(),now(),'STRING','DA File Name Boundary Classification',0,'da.file_name_boundary_classification',(select id from plugin where plugin_name='DOCUMENT_ASSEMBLER'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'UseDAGeneratedDocument',(select id from plugin_config where config_name='da.file_name_boundary_classification'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'MergeDocumentsBelongingToSameFile',(select id from plugin_config where config_name='da.file_name_boundary_classification'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'CreateNewDocumentForDifferentFile',(select id from plugin_config where config_name='da.file_name_boundary_classification'));

/*Updating isSearchable default values*/
UPDATE batch_class_dynamic_plugin_config SET isSearchable=b'1' WHERE isSearchable is NULL;

INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'500',(select id from plugin_config where config_name='validation.fuzzy_search_pop_up_x_dimension'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'350',(select id from plugin_config where config_name='validation.fuzzy_search_pop_up_y_dimension'));