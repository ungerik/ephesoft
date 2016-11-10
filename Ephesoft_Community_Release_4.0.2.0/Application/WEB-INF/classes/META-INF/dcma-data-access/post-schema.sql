
update kv_page_process set no_of_words = 0 where no_of_words is null ;
update kv_extraction set no_of_words = 0 where no_of_words is null;

/* Update query setting CMIS version 1.1 by default on upgrade. */
update batch_class_cmis_configuration set cmis_version = "1.1" where cmis_version is null;

/*Queries removed, since it was a duplicate constraint*/
/*ALTER TABLE module ADD UNIQUE (module_name)*/
/*ALTER TABLE plugin_config ADD UNIQUE (config_name)*/

update batch_class_plugin_config set plugin_config_value = null where plugin_config_value = 'Not Mapped yet.';


update plugin_config set is_mandatory = 0 where config_name = 'validation.url(Ctrl+4)';

update plugin_config set is_mandatory = 0 where config_name = 'validation.url(Ctrl+7)';

update plugin_config set is_mandatory = 0 where config_name = 'validation.url(Ctrl+8)';

update plugin_config set is_mandatory = 0 where config_name = 'validation.url(Ctrl+9)';

update plugin_config set is_mandatory = 0 where config_name = 'validation.url1_title';

update plugin_config set is_mandatory = 0 where config_name = 'validation.url2_title';

update plugin_config set is_mandatory = 0 where config_name = 'validation.url3_title';

update plugin_config set is_mandatory = 0 where config_name = 'validation.url4_title';


/*updating plugin config for for external application in review module*/

update plugin_config set is_mandatory = 0 where config_name = 'review.url(Ctrl+4)';

update plugin_config set is_mandatory = 0 where config_name = 'review.url(Ctrl+7)';

update plugin_config set is_mandatory = 0 where config_name = 'review.url(Ctrl+8)';

update plugin_config set is_mandatory = 0 where config_name = 'review.url(Ctrl+9)';

update plugin_config set is_mandatory = 0 where config_name = 'review.url1_title';

update plugin_config set is_mandatory = 0 where config_name = 'review.url2_title';

update plugin_config set is_mandatory = 0 where config_name = 'review.url3_title';

update plugin_config set is_mandatory = 0 where config_name = 'review.url4_title';

update plugin_config set is_mandatory = 0 where config_name = 'review.x_dimension';

update plugin_config set is_mandatory = 0 where config_name = 'review.y_dimension';

/*updating plugin config for for copy batch xml plugin in export module*/

update plugin_config set is_mandatory = 0 where config_name ='batch.folder_name';
update plugin_config set is_mandatory = 0 where config_name ='batch.file_name';
update plugin_config set is_mandatory = 1 where config_name ='batch.batch_xml_export_folder';


/*updating plugin config for for fuzzy DB plugin in extraction module*/

update plugin_config set is_mandatory = 0 where config_name ='fuzzydb.search.columnName';

/*updating fuzzydb search switch for fuzzy DB plugin in extraction module*/
   
 update plugin_config set is_mandatory = 1 where config_name ='fuzzydb.search.switch';

 update plugin_config set is_mandatory = 0 where config_name = 'cmis.repository_id';





update plugin_config set is_mandatory = 0 where config_name = 'createMultipageTif.optimization_parameters';

update plugin_config set is_mandatory = 0 where config_name = 'tabbedPdf.optimization_parameters';

update plugin_config set is_mandatory = 0 where config_name = 'validation.fuzzy_search_pop_up_x_dimension';

update plugin_config set is_mandatory = 0 where config_name = 'validation.fuzzy_search_pop_up_y_dimension';

update plugin_config set is_mandatory = 0 where config_name = 'validation.x_dimension';

update plugin_config set is_mandatory = 0 where config_name = 'validation.y_dimension';







update plugin_config set config_desc = 'External Application X Dimension(in px)' where config_name = 'validation.x_dimension';

update plugin_config set config_desc = 'External Application Y Dimension(in px)' where config_name = 'validation.y_dimension';


update plugin_config set is_mandatory = 0 where config_name = 'imagemagick.open_input_image_parameters';

update plugin_config set is_mandatory = 0 where config_name = 'imagemagick.save_output_image_parameters';

update plugin_config set is_mandatory = 0 where config_name = 'filebound.separator';

update plugin_config set is_mandatory = 0 where config_name = 'filebound.division';





update batch_class_plugin bcp set bcp.plugin_id = (select plg.id from plugin plg where plg.workflow_name = 'Page_Process_Scripting_Plugin') where bcp.plugin_id = (select plg.id from plugin plg where plg.workflow_name = 'Scripting_Plugin') and bcp.batch_class_module_id in (select bcm.id from batch_class_module bcm where bcm.workflow_name like '%Page_Process%');

update batch_class_plugin bcp set bcp.plugin_id = (select plg.id from plugin plg where plg.workflow_name = 'Document_Assembler_Scripting_Plugin') where bcp.plugin_id = (select plg.id from plugin plg where plg.workflow_name = 'Scripting_Plugin') and bcp.batch_class_module_id in (select bcm.id from batch_class_module bcm where bcm.workflow_name like '%Document_Assembler%');

update batch_class_plugin bcp set bcp.plugin_id = (select plg.id from plugin plg where plg.workflow_name = 'Automated_Validation_Scripting_Plugin') where bcp.plugin_id = (select plg.id from plugin plg where plg.workflow_name = 'Scripting_Plugin') and bcp.batch_class_module_id in (select bcm.id from batch_class_module bcm where bcm.workflow_name like '%Automated_Validation%');

update batch_class_plugin bcp set bcp.plugin_id = (select plg.id from plugin plg where plg.workflow_name = 'Extraction_Scripting_Plugin') where bcp.plugin_id = (select plg.id from plugin plg where plg.workflow_name = 'Scripting_Plugin') and bcp.batch_class_module_id in (select bcm.id from batch_class_module bcm where bcm.workflow_name like '%Extraction%');

update batch_class_plugin bcp set bcp.plugin_id = (select plg.id from plugin plg where plg.workflow_name = 'Export_Scripting_Plugin') where bcp.plugin_id = (select plg.id from plugin plg where plg.workflow_name = 'Scripting_Plugin') and bcp.batch_class_module_id in (select bcm.id from batch_class_module bcm where bcm.workflow_name like '%Export%');




update plugin_config set config_datatype = 'INTEGER' where config_name = 'regular.regex.confidence_score';

update plugin_config set config_datatype = 'INTEGER' where config_name = 'validation.x_dimension';

update plugin_config set config_datatype = 'INTEGER' where config_name = 'validation.y_dimension';

update plugin_config set config_datatype = 'INTEGER' where config_name = 'validation.fuzzy_search_pop_up_x_dimension';

update plugin_config set config_datatype = 'INTEGER' where config_name = 'validation.fuzzy_search_pop_up_y_dimension';


/*adding script name for scripting plugins*/

update plugin set script_name='ScriptExport' where plugin_name='EXPORT_SCRIPTING_PLUGIN';
update plugin set script_name='ScriptExtraction' where plugin_name='EXTRACTION_SCRIPTING_PLUGIN';
update plugin set script_name='ScriptDocumentAssembler' where plugin_name='DOCUMENT_ASSEMBLER_SCRIPTING_PLUGIN';
update plugin set script_name='ScriptAutomaticValidation' where plugin_name='AUTOMATED_VALIDATION_SCRIPTING_PLUGIN';
update plugin set script_name='ScriptPageProcessing' where plugin_name='PAGE_PROCESS_SCRIPTING_PLUGIN';



/*deleting table extraction plugin configs*/

delete from batch_class_plugin_config where plugin_config_id in (select id from plugin_config where config_name='tableextarction.extraction_method');

update batch_class set unc_folder='@@INSTALL_DIR_SQL@@SharedFolders/mailroom-import' where batch_class_name='MailroomAutomationTemplate';
update batch_class set unc_folder='@@INSTALL_DIR_SQL@@SharedFolders/searchablepdf-import' where batch_class_name='SearchablePDFTemplate';
update batch_class set unc_folder='@@INSTALL_DIR_SQL@@SharedFolders/grid-computing-import' where batch_class_name='GridComputingTemplate';

/*Updating for only templates*/
/*Final drop folder properties.*/
update batch_class_plugin_config set batch_class_plugin_config.plugin_config_value='@@INSTALL_DIR_SQL@@SharedFolders/final-drop-folder' 
where batch_class_plugin_config.plugin_config_id in (select plugin_config.id from plugin_config where plugin_config.config_name='batch.export_to_folder') 
and batch_class_plugin_config.batch_class_plugin_id in (select batch_class_plugin.id from batch_class_plugin where batch_class_plugin.batch_class_module_id in (select batch_class_module.id from batch_class_module where batch_class_module.workflow_name in ('Export_Module', 'Export_Module_BC3') and batch_class_module.batch_class_id in (
select batch_class.id from batch_class where batch_class.batch_class_name in ('MailroomAutomationTemplate', 'SearchablePDFTemplate', 'GridComputingTemplate'))) and batch_class_plugin.plugin_id in (
select plugin.id from plugin where plugin.plugin_name='COPY_BATCH_XML'));

/*Updating for only templates*/
/*CSV export folder properties.*/
update batch_class_plugin_config set batch_class_plugin_config.plugin_config_value='@@INSTALL_DIR_SQL@@SharedFolders/csv-export-folder' 
where batch_class_plugin_config.plugin_config_id in (select plugin_config.id from plugin_config where plugin_config.config_name='csvFileCreation.final_export_folder') 
and batch_class_plugin_config.batch_class_plugin_id in (select batch_class_plugin.id from batch_class_plugin where batch_class_plugin.batch_class_module_id in (select batch_class_module.id from batch_class_module where batch_class_module.workflow_name in ('Export_Module', 'Export_Module_BC3') and batch_class_module.batch_class_id in (
select batch_class.id from batch_class where batch_class.batch_class_name in ('MailroomAutomationTemplate', 'SearchablePDFTemplate', 'GridComputingTemplate'))) and batch_class_plugin.plugin_id in (
select plugin.id from plugin where plugin.plugin_name='CSV_FILE_CREATION_PLUGIN'));

/*Updating for only templates*/
/*IBM-CM export folder properties.*/
update batch_class_plugin_config set batch_class_plugin_config.plugin_config_value='@@INSTALL_DIR_SQL@@SharedFolders/ibm-cm-export-folder' 
where batch_class_plugin_config.plugin_config_id in (select plugin_config.id from plugin_config where plugin_config.config_name='ibmCm.final_export_folder') 
and batch_class_plugin_config.batch_class_plugin_id in (select batch_class_plugin.id from batch_class_plugin where batch_class_plugin.batch_class_module_id in (select batch_class_module.id from batch_class_module where batch_class_module.workflow_name in ('Export_Module', 'Export_Module_BC3') and batch_class_module.batch_class_id in (
select batch_class.id from batch_class where batch_class.batch_class_name in ('MailroomAutomationTemplate', 'SearchablePDFTemplate', 'GridComputingTemplate'))) and batch_class_plugin.plugin_id in (
select plugin.id from plugin where plugin.plugin_name='IBM_CM_PLUGIN'));

/*Updating for only templates*/
/*Export script properties for property tabbedPdf.property_file.*/
update batch_class_plugin_config set batch_class_plugin_config.plugin_config_value='@@INSTALL_DIR_SQL@@SharedFolders/export-script.properties' 
where batch_class_plugin_config.plugin_config_id in (select plugin_config.id from plugin_config where plugin_config.config_name='tabbedPdf.property_file') 
and batch_class_plugin_config.batch_class_plugin_id in (select batch_class_plugin.id from batch_class_plugin where batch_class_plugin.batch_class_module_id in (select batch_class_module.id from batch_class_module where batch_class_module.workflow_name in ('Export_Module', 'Export_Module_BC3') and batch_class_module.batch_class_id in (
select batch_class.id from batch_class where batch_class.batch_class_name in ('MailroomAutomationTemplate', 'SearchablePDFTemplate', 'GridComputingTemplate'))) and batch_class_plugin.plugin_id in (
select plugin.id from plugin where plugin.plugin_name='TABBED_PDF'));

/*Updating for only templates*/
/*Export script properties fro property tabbedPdf.final_export_folder.*/
update batch_class_plugin_config set batch_class_plugin_config.plugin_config_value='@@INSTALL_DIR_SQL@@SharedFolders/tabbed-pdf-export-folder' 
where batch_class_plugin_config.plugin_config_id in (select plugin_config.id from plugin_config where plugin_config.config_name='tabbedPdf.final_export_folder') 
and batch_class_plugin_config.batch_class_plugin_id in (select batch_class_plugin.id from batch_class_plugin where batch_class_plugin.batch_class_module_id in (select batch_class_module.id from batch_class_module where batch_class_module.workflow_name in ('Export_Module', 'Export_Module_BC3') and batch_class_module.batch_class_id in (
select batch_class.id from batch_class where batch_class.batch_class_name in ('MailroomAutomationTemplate', 'SearchablePDFTemplate', 'GridComputingTemplate'))) and batch_class_plugin.plugin_id in (
select plugin.id from plugin where plugin.plugin_name='TABBED_PDF'));

/*Updating for only templates*/
/*Export script properties fro property tabbedPdf.final_export_folder.*/
update batch_class_plugin_config set batch_class_plugin_config.plugin_config_value='@@INSTALL_DIR_SQL@@SharedFolders/NSI-export-folder' 
where batch_class_plugin_config.plugin_config_id in (select plugin_config.id from plugin_config where plugin_config.config_name='nsi.final_export_folder') 
and batch_class_plugin_config.batch_class_plugin_id in (select batch_class_plugin.id from batch_class_plugin where batch_class_plugin.batch_class_module_id in (select batch_class_module.id from batch_class_module where batch_class_module.workflow_name in ('Export_Module', 'Export_Module_BC3') and batch_class_module.batch_class_id in (
select batch_class.id from batch_class where batch_class.batch_class_name in ('MailroomAutomationTemplate', 'SearchablePDFTemplate', 'GridComputingTemplate'))) and batch_class_plugin.plugin_id in (
select plugin.id from plugin where plugin.plugin_name='NSI_EXPORT'));

/*Updation of create compare thumbnail property in BC1 while upgradation.*/
update batch_class_plugin_config set batch_class_plugin_config.plugin_config_value='OFF' 
where batch_class_plugin_config.plugin_config_id in (select plugin_config.id from plugin_config where plugin_config.config_name='createThumbnails.switch') 
and batch_class_plugin_config.batch_class_plugin_id in (select batch_class_plugin.id from batch_class_plugin where batch_class_plugin.batch_class_module_id in (select batch_class_module.id from batch_class_module where batch_class_module.workflow_name in ('Recostar_Page_Process_Module') and batch_class_module.batch_class_id in (
select batch_class.id from batch_class where batch_class.batch_class_name in ('MailroomAutomationTemplate'))) and batch_class_plugin.plugin_id in (
select plugin.id from plugin where plugin.plugin_name='CREATE_THUMBNAILS'));

CREATE INDEX IX_BATCH_CLASS_PLUGIN_CONFIG_BATCH ON batch_class_plugin_config (batch_class_plugin_id);

CREATE INDEX IX_BATCH_INSTANCE_IDENTIFIER ON batch_instance (identifier);

CREATE INDEX IX_FUNCTION_KEY_DOCUMENT ON function_key (document_type_id);

CREATE INDEX IX_PAGE_TYPE_DOCUMENT ON page_type (document_type_id);

/*To make progress status retrieval faster from history.*/
CREATE INDEX IX_HISTORY_NAME on ACT_HI_VARINST (NAME_);

/*Regex pool queries.*/
insert into regex_group (creation_date,last_modified,name,type) VALUES (now(),now(),"Invoice number", null);
insert into regex_group (creation_date,last_modified,name,type) VALUES (now(),now(),"Invoice terms", null);
insert into regex_group (creation_date,last_modified,name,type) VALUES (now(),now(),"Date/ Time", null);
insert into regex_group (creation_date,last_modified,name,type) VALUES (now(),now(),"Amount", null);
insert into regex_group (creation_date,last_modified,name,type) VALUES (now(),now(),"Email", null);
insert into regex_group (creation_date,last_modified,name,type) VALUES (now(),now(),"Address", null);
insert into regex_group (creation_date,last_modified,name,type) VALUES (now(),now(),"Financial", null);
insert into regex_group (creation_date,last_modified,name,type) VALUES (now(),now(),"Number", null);
insert into regex_group (creation_date,last_modified,name,type) VALUES (now(),now(),"Common regex", null);

insert into regex_pattern (creation_date,last_modified,description,pattern,regex_group_id) VALUES (now(),now(),"INVOICE NUMBER: value with maybe a capital letter at start, then 3 to 10 times occurrence of a digit or hyphen.", "([A-z]?[0-9-]{3,10})", (select id from regex_group where name = "Invoice number"));
insert into regex_pattern (creation_date,last_modified,description,pattern,regex_group_id) VALUES (now(),now(),"INVOICE NUMBER: value with 4 to 10 digits", "\\d{4,10}", (select id from regex_group where name = "Invoice number"));
insert into regex_pattern (creation_date,last_modified,description,pattern,regex_group_id) VALUES (now(),now(),"INVOICE NUMBER: value that can be a word with any combination of numbers, letters, and dashes of length 1 to 25 characters, excluding 'and' word. This works with negative lookahead for 'and'.","^(?!(and))[A-z0-9-]{1,25}$", (select id from regex_group where name = "Invoice number"));
insert into regex_pattern (creation_date,last_modified,description,pattern,regex_group_id) VALUES (now(),now(),"INVOICE NUMBER: value with 5 to 15 digits, may have 1 to 5 alphabets/hyphen in left and right.", "[A-Z\\-]{1,5}\\d{4,15}[A-Z\\-]{1,5}|[A-Z\\-]{1,5}\\d{4,15}|\\d{3,15}", (select id from regex_group where name = "Invoice number"));
insert into regex_pattern (creation_date,last_modified,description,pattern,regex_group_id) VALUES (now(),now(),"INVOICE NUMBER: value with maybe a capital letter at start, then 3 to 10 times occurrence of a digit or hyphen.", "([A-Z]?[0-9\\-]{3,10})", (select id from regex_group where name = "Invoice number"));
insert into regex_pattern (creation_date,last_modified,description,pattern,regex_group_id) VALUES (now(),now(),"INVOICE NUMBER: Case insensitive match for invoice number key.", "(?i:(inv(oice)?|inv#|Invoice#)\\s(no\\.|number|#|/Trans\\.)?(?<!invoice\\s))", (select id from regex_group where name = "Invoice number"));
insert into regex_pattern (creation_date,last_modified,description,pattern,regex_group_id) VALUES (now(),now(),"INVOICE NUMBER: This regex will match many of the common ways of writing the word number. The regex will then discard any matches of number if it has one of the key words before it such as PO, or Service etc.", "(?i:(?<!ACCOUNT |DAN |MFG\\. |ACCT\\. |despatch |CUST\\. |CUSTOMER |DELIVERY |ORDER |P\\.O\\. |PO |REF |REF\\. |REFERENCE |SERVICE |TRACKING )INV(OICE|\\.)? (#|(N(O\\.?|UM(BER|\\.)?))))", (select id from regex_group where name = "Invoice number"));

insert into regex_pattern (creation_date,last_modified,description,pattern,regex_group_id) VALUES (now(),now(),"SHIPPED VIA: this Regex will match a few of the popular shipping providers", "USPS|UPS|FEDEX|DHL|ELECTRIONIC", (select id from regex_group where name = "Invoice terms"));
insert into regex_pattern (creation_date,last_modified,description,pattern,regex_group_id) VALUES (now(),now(),"NET TERMS: This will match the word Net then a space then any two numbers or a digit followed by a / then another digit. It will also match other common invoice terms such as COD, Pre-Paid, etc.", "(?i:(NET) \\d{2}|\\d\\/\\d|COD|Credit Card|Not Assigned|UNKNOWN TERMS|Pre-Paid|Discount Not Applicable)", (select id from regex_group where name = "Invoice terms"));

insert into regex_pattern (creation_date,last_modified,description,pattern,regex_group_id) VALUES (now(),now(),"Simple Date: Date with format of DD\\MM\\YYYY","\\d{2}/\\d{2}/\\d{4}", (select id from regex_group where name = "Date/ Time"));

insert into regex_pattern (creation_date,last_modified,description,pattern,regex_group_id) VALUES (now(),now(),"Simple US Date: Date with format of DD Month/MM YYYY or DD-Month/MM-YYYY or DD/(Month/MM)/YYYY or DD.Month/MM.YYYY","\\d{1,2}[ /\\-\\.]\\d{1,2}[ /\\-\\.]\\d{2,4}|\\d{2}[ \\-\\.](?i:(Jan|Mar|MAY|Jul|Sep(t)?|Nov|Feb|Apr|Jun|Aug|Oct|Dec))[ \\-/\\.]\\d{2,4}", (select id from regex_group where name = "Date/ Time"));
insert into regex_pattern (creation_date,last_modified,description,pattern,regex_group_id) VALUES (now(),now(),"US Date: Date with format of d MM/DD/YYYY or MM-DD-YYYY or MM.DD.YYYY","(0[1-9]|1[012])[-/\\.](0[1-9]|[12][0-9]|3[01])[-/\\.]\\d\\d([0-9]{2})?", (select id from regex_group where name = "Date/ Time"));
insert into regex_pattern (creation_date,last_modified,description,pattern,regex_group_id) VALUES (now(),now(),"Date with format of DD Month YYYY","\\d{2}(-|\\s)(?i:(January|Febuary|March|April|May|June|July|Aug(ust)?|September|oct(ober)?|November|December))(-|\\s)\\d{2,4}", (select id from regex_group where name = "Date/ Time"));
insert into regex_pattern (creation_date,last_modified,description,pattern,regex_group_id) VALUES (now(),now(),"Non US Date: Date with format of d DD\\MM\\YYYY or DD-MM-YYYY or DD.MM.YYYY","((0[1-9]|[12][0-9]|3[01])[-/\\.]0[1-9]|1[012])[-/\\.]\\d\\d([0-9]{2})?", (select id from regex_group where name = "Date/ Time"));
insert into regex_pattern (creation_date,last_modified,description,pattern,regex_group_id) VALUES (now(),now(),"Look back: ", "(?<!Due\\s)Date", (select id from regex_group where name = "Date/ Time"));
insert into regex_pattern (creation_date,last_modified,description,pattern,regex_group_id) VALUES (now(),now(),"DATE TIME: This regex will match 12/25/2003 or 08:03:31 or 02/29/2004 12 AM", "(?=\\d)(?:(?:(?:(?:(?:0?[13578]|1[02])(\\/|-|\\.)31)\\1|(?:(?:0?[1,3-9]|1[0-2])(\\/|-|\\.)(?:29|30)\\2))(?:(?:1[6-9]|[2-9]\\d)?\\d{2})|(?:0?2(\\/|-|\\.)29\\3(?:(?:(?:1[6-9]|[2-9]\\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00))))|(?:(?:0?[1-9])|(?:1[0-2]))(\\/|\\-|\\.)(?:0?[1-9]|1\\d|2[0-8])\\4(?:(?:1[6-9]|[2-9]\\d)?\\d{2}))($|\\ (?=\\d)))?(((0?[1-9]|1[012])(:[0-5]\\d){0,2}(\\ [AP]M))|([01]\\d|2[0-3])(:[0-5]\\d){1,2})?", (select id from regex_group where name = "Date/ Time"));
insert into regex_pattern (creation_date,last_modified,description,pattern,regex_group_id) VALUES (now(),now(),"DAY OF THE WEEK: This regex will match the days of the week in both their short hand and long hand format.", "(Sun|Mon|(T(ues|hurs))|Fri)(day|\\.)?$|Wed(\\.|nesday)?$|Sat(\\.|urday)?$|T((ue?)|(hu?r?))\\.?", (select id from regex_group where name = "Date/ Time"));
insert into regex_pattern (creation_date,last_modified,description,pattern,regex_group_id) VALUES (now(),now(),"MONTHS: This regex will match the full name of the months", "(?:J(anuary|u(ne|ly))|February|Ma(rch|y)|A(pril|ugust)|(((Sept|Nov|Dec)em)|Octo)ber)", (select id from regex_group where name = "Date/ Time"));

insert into regex_pattern (creation_date,last_modified,description,pattern,regex_group_id) VALUES (now(),now(),"USA DATE COMPLEX: Will match month names in their long or short hand formats. All Date names are case insensitive.", "(?i:((Jan)(uary)?|(Feb)(ruary)?|Mar(ch)?|April|May|June|July|Aug(ust)?|(Sept|Nov|Dec)(ember)?|oct(ober)?))((-|\\s)\\d{2,4}\\,?\\s?\\d{4})|\\d{2} (?i:((Jan)(uary)?|(Feb)(ruary)?|Mar(ch)?|April|May|June|July|Aug(ust)?|(Sept|Nov|Dec)(ember)?|oct(ober)?)) \\d{4}", (select id from regex_group where name = "Date/ Time"));

insert into regex_pattern (creation_date,last_modified,description,pattern,regex_group_id) VALUES (now(),now(),"Format for Non US amount.", "((\\d{1,3}[\\.]{0,1}){0,4}\\d{1,3})?([,]\\d{1,2})?", (select id from regex_group where name = "Amount"));
insert into regex_pattern (creation_date,last_modified,description,pattern,regex_group_id) VALUES (now(),now(),"1 to 5 digits followed by a decimal and 2 digits.","\\d{1,5}[\\.]{0,1}\\d{2}", (select id from regex_group where name = "Amount"));
insert into regex_pattern (creation_date,last_modified,description,pattern,regex_group_id) VALUES (now(),now(),"Format for US amount", "\\$?\\s*((\\d{1,3}[,]{0,1}){0,4}\\d{1,3})?([\\.]\\d{1,2})?", (select id from regex_group where name = "Amount"));
insert into regex_pattern (creation_date,last_modified,description,pattern,regex_group_id) VALUES (now(),now(),"BILL TO: This regex will match the word BILL TO or other common ways of writing Bill to for example Invoice To or Customer Address etc.", "(?i:(?<!This |\\w)(Bill[\\- ]?To|Sold To|Customer Address|Invoicing Adress|BILL|Invoiceto:))", (select id from regex_group where name = "Amount"));
insert into regex_pattern (creation_date,last_modified,description,pattern,regex_group_id) VALUES (now(),now(),"INVOICE TOTAL: This will match Total or Balance but it will not match total or Balance if the word Qty, or Units follow it. It will also not match Merchandise if it come before the word Total or Balance", "(?i:(?<!Merchandise )(Total|balance)(?! Qty| Units))", (select id from regex_group where name = "Amount"));

insert into regex_pattern (creation_date,last_modified,description,pattern,regex_group_id) VALUES (now(),now(),"EMAIL ADDRESS: having one or more letters/digits/dot/hyphen followed by @ symbol, then digit/letter/dot/hyphen followed by dot and end letters.", "^([a-zA-Z0-9_.-]+)@([A-Za-z0-9.-]+)\\.([a-zA-Z]{2,6})$", (select id from regex_group where name = "Email"));


insert into regex_pattern (creation_date,last_modified,description,pattern,regex_group_id) VALUES (now(),now(),"SOCIAL SECURITY NUMBER: will match valid SSN in this format XXX-XX-XXXX", "(?!(000|666|9))\\d{3}-(?!00)\\d{2}-(?!0000)\\d{4}", (select id from regex_group where name = "Financial"));
insert into regex_pattern (creation_date,last_modified,description,pattern,regex_group_id) VALUES (now(),now(),"CREDIT SCORE: This will match valid Credit Score Numbers", "(?<!([1-9][1-9]|[1-9]))([3-7][0-9][0-9]|8[0-4][0-9]|850)(?!\\d)", (select id from regex_group where name = "Financial"));

insert into regex_pattern (creation_date,last_modified,description,pattern,regex_group_id) VALUES (now(),now(),"EQUIFAX SCORE: This will match Equifax keys such as Beacon 5.0 or EFX FACTA etc.", "(?i:((EFX\\sFACTA)|Equifax(\\/Facta)?)|(\\s)?(Beacon)\\s?5?(\\.0)?)", (select id from regex_group where name = "Financial"));


insert into regex_pattern (creation_date,last_modified,description,pattern,regex_group_id) VALUES (now(),now(),"Phone number", "\\(?([0-9]{3})\\)?[-\\. ]?([0-9]{3})[-\\. ]?([0-9]{4})$", (select id from regex_group where name = "Number"));
insert into regex_pattern (creation_date,last_modified,description,pattern,regex_group_id) VALUES (now(),now(),"PO NUMBER: This Regex will match any of the different cases of Purchase Order, such as PO, P.O. Number, Customer PO etc. But it will not match P.O. if the word Box comes after it.", "(?i:(purchase( order)?|PO|P\\.0\\.|P\\.O\\.|Customer PO:?|CUST\\.P\\.0\\.|CUST\\.P\\.O\\.|P\\.O\\.#\\:|Cust\\. PO#)(\\s)(no\\.|number|#)?(?!BOX))", (select id from regex_group where name = "Number"));

insert into regex_pattern (creation_date,last_modified,description,pattern,regex_group_id) VALUES (now(),now(),"ANY VALUE: this will capture any value any number of times. Catch All expression", ".+", (select id from regex_group where name = "Common regex"));
insert into regex_pattern (creation_date,last_modified,description,pattern,regex_group_id) VALUES (now(),now(),"Any Non Digit", "[^0-9]|\\D", (select id from regex_group where name = "Common regex"));
insert into regex_pattern (creation_date,last_modified,description,pattern,regex_group_id) VALUES (now(),now(),"Any Whitespace character", "[ \\t\\n\\x0B\\f\\r]|\\s", (select id from regex_group where name = "Common regex"));
insert into regex_pattern (creation_date,last_modified,description,pattern,regex_group_id) VALUES (now(),now(),"Any Digit", "\\d|[0-9]", (select id from regex_group where name = "Common regex"));
insert into regex_pattern (creation_date,last_modified,description,pattern,regex_group_id) VALUES (now(),now(),"Any Non Whitespace character", "[^\\s]|\\S", (select id from regex_group where name = "Common regex"));
insert into regex_pattern (creation_date,last_modified,description,pattern,regex_group_id) VALUES (now(),now(),"Any Word character", "[a-zA-Z\\ 0-9]|\\w", (select id from regex_group where name = "Common regex"));
insert into regex_pattern (creation_date,last_modified,description,pattern,regex_group_id) VALUES (now(),now(),"Any Non-Word character", "[^\\w]|\\W", (select id from regex_group where name = "Common regex"));


insert into regex_pattern (creation_date,last_modified,description,pattern,regex_group_id) VALUES (now(),now(), "US Zip Code","^\\d{5}(-\\d{4})?$", (select id from regex_group where name = "Address"));
insert into regex_pattern (creation_date,last_modified,description,pattern,regex_group_id) VALUES (now(),now(), "LAST LINE OF USA ADDRESS: This will match the last line of a US Address. It will look for the City then the State in its two letter abbreviation then the 5 digit Zip code","[A-z0-9\\.\\, ]+,?\\s?(?:A[KLRZ]|C[AOT]|D[CE]|FL|GA|HI|I[ADLN]|K[SY]|LA|M[ADEINOST]|N[CDEHJMVY]|O[HKR]|PA|RI|S[CD]|T[NX]|UT|V[AT]|W[AIVY])(\\s| - )[0-9]{4,5}(?:-[0-9]{4})?", (select id from regex_group where name = "Address"));
insert into regex_pattern (creation_date,last_modified,description,pattern,regex_group_id) VALUES (now(),now(), "Bill To","(?i:(?<!This |\\w)(Bill[- ]?To|Sold To|Customer Address|Invoicing Address|BILL|Invoiceto:))", (select id from regex_group where name = "Address"));
insert into regex_pattern (creation_date,last_modified,description,pattern,regex_group_id) VALUES (now(),now(), "STATE CODES: This Regex will match all the state two letter abbreviations","(?-i:A[LKSZRAEP]|C[AOT]|D[EC]|F[LM]|G[AU]|HI|I[ADLN]|K[SY]|LA|M[ADEHINOPST]|N[CDEHJMVY]|O[HKR]|P[ARW]|RI|S[CD]|T[NX]|UT|V[AIT]|W[AIVY])", (select id from regex_group where name = "Address"));

/*Setting the property in EXPORT_TO_HPII_FILENET plugin.*/
update batch_class_plugin_config set batch_class_plugin_config.plugin_config_value='@@INSTALL_DIR_SQL@@SharedFolders/Configuration' 
where batch_class_plugin_config.plugin_config_id in (select plugin_config.id from plugin_config where plugin_config.config_name='exporttoHPIIfilenet.config_file_path') 
and batch_class_plugin_config.batch_class_plugin_id in (select batch_class_plugin.id from batch_class_plugin where batch_class_plugin.batch_class_module_id in (select batch_class_module.id from batch_class_module where batch_class_module.workflow_name in ('Export_Module', 'Export_Module_BC3') and batch_class_module.batch_class_id in (
select batch_class.id from batch_class)) and batch_class_plugin.plugin_id in (
select plugin.id from plugin where plugin.plugin_name='EXPORT_TO_HPII_FILENET'));

/*Updating the property of scanner master configuration.*/
delete from scanner_master_config_sample_value where sample_value = 5 and master_config_id in (select id from scanner_master_configuration where description in ('Color','Current Pixel Type'));

/*Fuzzy Db properties setting according to user requirements. 3.1 and above*/
/*Connection URL*/
update 
batch_class_plugin_config set plugin_config_value='jdbc:mysql://@@MYSQL_SERVERNAME@@:@@MYSQL_PORT@@/@@APPLICATION_DB_NAME@@'
where plugin_config_id IN (
                                select id 
                                 from plugin_config 
                                 where 
                                 config_name='fuzzydb.database.connectionURL'
                                ) AND batch_class_plugin_id IN
    
   (  select id from batch_class_plugin 
                    where
                                plugin_id 
                                 IN
                                (
                                select id 
                                 from plugin 
                                 where 
                                 plugin_name='FUZZYDB'
                                )
                                AND 
                                 batch_class_module_id 
                                 IN
                                (
                                select id 
                                 from batch_class_module 
                                 where module_id 
                                 IN 
                                 (
                                select id 
                                 from module 
                                 where module_name='Extraction'
                                )
                                AND
                                batch_class_id 
                                 IN
                                (
                                select id 
                                 from batch_class 
                                 where batch_class_name in ('MailroomAutomationTemplate', 'SearchablePDFTemplate', 'GridComputingTemplate')
                                 )
                                )
                );

/*Db Export properties setting according to user requirements. 3.1 and above*/
/*Connection URL*/
update 
batch_class_plugin_config set plugin_config_value='jdbc:mysql://@@MYSQL_SERVERNAME@@:@@MYSQL_PORT@@/@@APPLICATION_DB_NAME@@'
where plugin_config_id IN (
                                select id 
                                 from plugin_config 
                                 where 
                                 config_name='dbExport.database.connectionURL'
                                ) AND batch_class_plugin_id IN
    
   (  select id from batch_class_plugin 
                    where
                                plugin_id 
                                 IN
                                (
                                select id 
                                 from plugin 
                                 where 
                                 plugin_name='DB_EXPORT'
                                )
                                AND 
                                 batch_class_module_id 
                                 IN
                                (
                                select id 
                                 from batch_class_module 
                                 where module_id 
                                 IN 
                                 (
                                select id 
                                 from module 
                                 where module_name='Export'
                                )
                                AND
                                batch_class_id 
                                 IN
                                (
                                select id 
                                 from batch_class 
                                 where batch_class_name in ('MailroomAutomationTemplate', 'SearchablePDFTemplate', 'GridComputingTemplate')
                                 )
                                )
                );


update kv_extraction Set extract_zone = 'ALL' Where extract_zone is NULL AND fetch_value is not NULL;

/*Setting of default weight/multiplier as 1.0 for kv extraction if its not null*/
update kv_extraction set multiplier = 1.0 where multiplier is null;

/*Setting value of ocr confidence threshold to 90 if null*/
update field_type set ocr_confidence_threshold = 90 where ocr_confidence_threshold is null;

/*deleting the values in case of upgrade only from BC1.*/
delete from batch_class_plugin_config where batch_class_plugin_id in (select id from batch_class_plugin where batch_class_module_id in (select id from batch_class_module where batch_class_id in (select id from batch_class where batch_class_name = 'MailroomAutomationTemplate') and module_id in (select id from module where module_name = 'Page Process')) and plugin_id in (select id from plugin where plugin_name in ('CLASSIFY_IMAGES', 'CLASSIFY_IMAGES', 'HTML_TO_XML', 'BARCODE_READER', 'KV_PAGE_PROCESS', 'PAGE_PROCESS_SCRIPTING_PLUGIN')));
delete from batch_class_plugin where batch_class_module_id in (select id from batch_class_module where batch_class_id in (select id from batch_class where batch_class_name = 'MailroomAutomationTemplate') and module_id in (select id from module where module_name = 'Page Process')) and plugin_id in (select id from plugin where plugin_name in ('CLASSIFY_IMAGES', 'CLASSIFY_IMAGES', 'HTML_TO_XML', 'BARCODE_READER', 'KV_PAGE_PROCESS', 'PAGE_PROCESS_SCRIPTING_PLUGIN')); 
delete from batch_class_plugin_config where batch_class_plugin_id in (select id from batch_class_plugin where batch_class_module_id in (select id from batch_class_module where batch_class_id in (select id from batch_class where batch_class_name = 'MailroomAutomationTemplate') and module_id in (select id from module where module_name = 'Export')) and plugin_id in (select id from plugin where plugin_name in ('EXPORT_SCRIPTING_PLUGIN', 'TABBED_PDF', 'IBM_CM_PLUGIN', 'DOCUSHARE_EXPORT', 'FILEBOUND_EXPORT', 'NSI_EXPORT', 'KEY_VALUE_LEARNING_PLUGIN', 'DB_EXPORT')));
delete from batch_class_plugin where batch_class_module_id in (select id from batch_class_module where batch_class_id in (select id from batch_class where batch_class_name = 'MailroomAutomationTemplate') and module_id in (select id from module where module_name = 'Export')) and plugin_id in (select id from plugin where plugin_name in ('EXPORT_SCRIPTING_PLUGIN', 'TABBED_PDF', 'IBM_CM_PLUGIN', 'DOCUSHARE_EXPORT', 'FILEBOUND_EXPORT', 'NSI_EXPORT', 'KEY_VALUE_LEARNING_PLUGIN', 'DB_EXPORT')); 

/*setting the default sequence of order number in table_columns_info table.*/
update table_columns_info set order_number=id where order_number is null;


update table_info set no_of_rows = 5 where no_of_rows is null;

update kv_extraction set order_number = id where order_number is null;

/*Adding queries for populating the data in table.*/
insert into  table_extraction_rule(creation_date, last_modified, end_pattern, start_pattern, table_api, table_info_id, rule_name) select table_info.creation_date, table_info.last_modified, table_info.end_pattern, table_info.start_pattern, table_info.table_extraction_api, table_info.id, 'Rule' from table_info where table_info.start_pattern IS NOT NULL;

update table_info set table_extraction_api = NULL, start_pattern = NULL , end_pattern = NULL where table_info.start_pattern IS NOT NULL;

update table_extraction_rule set table_api='REGEX_VALIDATION' where table_api is null;

INSERT INTO table_column_extraction_rule(creation_date, last_modified, between_left, between_right, column_coordinate_y0, column_coordinate_y1, column_end_coordinate, column_header_pattern, column_pattern, column_start_coordinate, extracted_column_name, is_currency, is_required, is_mandatory, order_number, table_extraction_rule_id, column_name)
SELECT table_columns_info.creation_date, table_columns_info.last_modified, table_columns_info.between_left, table_columns_info.between_right, table_columns_info.column_coordinate_y0, table_columns_info.column_coordinate_y1, table_columns_info.column_end_coordinate, table_columns_info.column_header_pattern, table_columns_info. column_pattern, table_columns_info.column_start_coordinate, table_columns_info.extracted_column_name, table_columns_info.is_currency, table_columns_info.is_required, table_columns_info.is_mandatory, table_columns_info.order_number, 
(
SELECT id
FROM table_extraction_rule
WHERE table_extraction_rule.table_info_id= table_columns_info.table_info_id), table_columns_info.column_name
FROM table_columns_info
WHERE (select COUNT(table_column_extraction_rule.id) from table_column_extraction_rule) = 0;

UPDATE table_columns_info SET between_left= NULL, between_right= NULL, column_coordinate_y0= NULL, column_coordinate_y1= NULL, column_end_coordinate= NULL, column_header_pattern= NULL, column_pattern= NULL, column_start_coordinate= NULL, extracted_column_name= NULL, is_currency= 0, is_required = 0, is_mandatory = 0, order_number = 0;

UPDATE table_column_extraction_rule SET is_currency = 0
WHERE is_currency IS NULL;

/*setting the default value of plugin config to rgb.*/
update batch_class_plugin_config set batch_class_plugin_config.plugin_config_value='-colorspace rgb' 
where batch_class_plugin_config.plugin_config_id in (select plugin_config.id from plugin_config where plugin_config.config_name='createThumbnails.output_image_parameters') 
and batch_class_plugin_config.batch_class_plugin_id in (select batch_class_plugin.id from batch_class_plugin where batch_class_plugin.batch_class_module_id in (select batch_class_module.id from batch_class_module where batch_class_module.workflow_name in ('Recostar_Page_Process_Module') and batch_class_module.batch_class_id in (
select batch_class.id from batch_class where batch_class.batch_class_name in ('MailroomAutomationTemplate', 'SearchablePDFTemplate', 'GridComputingTemplate'))) and batch_class_plugin.plugin_id in (
select plugin.id from plugin where plugin.plugin_name='CREATE_THUMBNAILS'));

/* setting default naunce.valid_extension to tif only */
update batch_class_plugin_config set batch_class_plugin_config.plugin_config_value='tif' 
where batch_class_plugin_config.plugin_config_id in (select plugin_config.id from plugin_config where plugin_config.config_name='nuance.valid_extensions') 
and batch_class_plugin_config.batch_class_plugin_id in (select batch_class_plugin.id from batch_class_plugin where batch_class_plugin.batch_class_module_id in (select batch_class_module.id from batch_class_module where batch_class_module.workflow_name in ('Recostar_Page_Process_Module', 'Page_Process_Module_BC3') and batch_class_module.batch_class_id in (
select batch_class.id from batch_class where batch_class.batch_class_name in ('MailroomAutomationTemplate', 'SearchablePDFTemplate', 'GridComputingTemplate'))) and batch_class_plugin.plugin_id in (
select plugin.id from plugin where plugin.plugin_name='NUANCE_HOCR'));

/*changes for encryption*/
insert into encryption_key_metadata (creation_date,last_modified,key_password,key_generated) values(now(),now(),null,0);

/* change for encryption upgrade issue */
delete from encryption_key_metadata where id not in (1);

/*Changing document assembly property desc*/
update plugin_config set config_desc = 'Regex Classification Default Document Type' where config_desc in ('Default Document Type');

/*updating default value of export folder and file name.*/
update batch_class_plugin_config set batch_class_plugin_config.plugin_config_value='@@INSTALL_DIR_SQL@@SharedFolders/$BATCH_CLASS/$BATCH_IDENTIFIER/$DATE(yyyy-MM-dd)' 
where batch_class_plugin_config.plugin_config_id in (select plugin_config.id from plugin_config where plugin_config.config_name='batch.multipage_export_folder_path') 
and batch_class_plugin_config.batch_class_plugin_id in (select batch_class_plugin.id from batch_class_plugin where batch_class_plugin.batch_class_module_id in (select batch_class_module.id from batch_class_module where batch_class_module.workflow_name in ('Export_Module', 'Export_Module_BC3') and batch_class_module.batch_class_id in (
select batch_class.id from batch_class where batch_class.batch_class_name in ('MailroomAutomationTemplate', 'SearchablePDFTemplate', 'GridComputingTemplate'))) and batch_class_plugin.plugin_id in (
select plugin.id from plugin where plugin.plugin_name='COPY_BATCH_XML'));

update batch_class_plugin_config set batch_class_plugin_config.plugin_config_value='$BATCH_CLASS&$BATCH_IDENTIFIER&$DATE(yyyy-MM-dd)&$TIME' 
where batch_class_plugin_config.plugin_config_id in (select plugin_config.id from plugin_config where plugin_config.config_name='batch.multipage_export_file_name') 
and batch_class_plugin_config.batch_class_plugin_id in (select batch_class_plugin.id from batch_class_plugin where batch_class_plugin.batch_class_module_id in (select batch_class_module.id from batch_class_module where batch_class_module.workflow_name in ('Export_Module', 'Export_Module_BC3') and batch_class_module.batch_class_id in (
select batch_class.id from batch_class where batch_class.batch_class_name in ('MailroomAutomationTemplate', 'SearchablePDFTemplate', 'GridComputingTemplate'))) and batch_class_plugin.plugin_id in (
select plugin.id from plugin where plugin.plugin_name='COPY_BATCH_XML'));

/* Creation date set for default batch class */
update batch_class set creation_date = NOW() where identifier = 'BC1';
update batch_class set creation_date = NOW() where identifier = 'BC2';
update batch_class set creation_date = NOW() where identifier = 'BC3';

/* Testing table features */
update table_column_extraction_rule tcer set tcer.table_columns_info_id =
(select tci.id as table_columns_info_id from
table_extraction_rule ter, table_info ti, table_columns_info tci where
tcer.table_extraction_rule_id = ter.id and ter.table_info_id = ti.id and
ti.id = tci.table_info_id and RTRIM(LTRIM(tcer.column_name)) = RTRIM(LTRIM(tci.column_name))
and tcer.table_columns_info_id is null limit 1)
where tcer.table_columns_info_id is null;

/* Rename classification */
update plugin_config_sample_value set sample_value = 'OneDocumentClassification' where sample_value='SearchablePdfClassification' and plugin_config_id=(select id from plugin_config where config_name='da.factory_classification');

update batch_class_plugin_config set batch_class_plugin_config.plugin_config_value='OneDocumentClassification' 
where batch_class_plugin_config.plugin_config_id in (select plugin_config.id from plugin_config where plugin_config.config_name='da.factory_classification') 
and batch_class_plugin_config.batch_class_plugin_id in (select batch_class_plugin.id from batch_class_plugin where batch_class_plugin.batch_class_module_id in (select batch_class_module.id from batch_class_module where batch_class_module.workflow_name in ('Document_Assembler_Module') and batch_class_module.batch_class_id in (
select batch_class.id from batch_class where batch_class.batch_class_name in ('SearchablePDFTemplate'))) and batch_class_plugin.plugin_id in (
select plugin.id from plugin where plugin.plugin_name='DOCUMENT_ASSEMBLER'));

/* Reports query*/
insert into reports_folder (creation_date, last_modified, host_uri_path, default_context_path) values (NOW(), NOW(), null, '/EphesoftReports/rdPage.aspx?rdReport=');

insert into report (creation_date, last_modified, report_name, default_folder_path, parent_folder_id) values (NOW(), NOW(), 'Dashboard Reports', 'Dashboard', (select id from reports_folder where default_context_path = "/EphesoftReports/rdPage.aspx?rdReport="));
insert into report (creation_date, last_modified, report_name, default_folder_path, parent_folder_id) values (NOW(), NOW(), 'Throughput Reports', 'Throughput_Reports', (select id from reports_folder where default_context_path = "/EphesoftReports/rdPage.aspx?rdReport="));
insert into report (creation_date, last_modified, report_name, default_folder_path, parent_folder_id) values (NOW(), NOW(), 'Advanced Reports', 'Advanced_Reports', (select id from reports_folder where default_context_path = "/EphesoftReports/rdPage.aspx?rdReport="));
insert into report (creation_date, last_modified, report_name, default_folder_path, parent_folder_id) values (NOW(), NOW(), 'Analysis Grid', 'Analysis_Grid', (select id from reports_folder where default_context_path = "/EphesoftReports/rdPage.aspx?rdReport="));

update sub_report set report_name='Batch Class Throughput' where default_report_path="Per_Batchclass";
update sub_report set report_name='Batch Size Throughput' where default_report_path="Per_Batchsize";
update sub_report set report_name='Week Days Throughput' where default_report_path="Per_Weekdays";
update sub_report set report_name='Hourly Throughput' where default_report_path="Per_Hourly";
update sub_report set report_name='Weekly Throughput' where default_report_path="Per_Weekly";
update sub_report set report_name='Monthly Throughput' where default_report_path="Per_Monthly";
update sub_report set report_name='Module Throughput' where default_report_path="Per_Module";
update sub_report set report_name='User Throughput' where default_report_path="Processing_Per_User";

insert into sub_report (creation_date, last_modified, report_name, default_report_path, parent_folder_id, parent_id) values (NOW(), NOW(), 'Dashboard', 'Dashboard', (select id from report where default_folder_path = "Dashboard"), null);
insert into sub_report (creation_date, last_modified, report_name, default_report_path, parent_folder_id, parent_id) values (NOW(), NOW(), 'Batch Class Throughput', 'Per_Batchclass', (select id from report where default_folder_path = "Throughput_Reports"), null);
insert into sub_report (creation_date, last_modified, report_name, default_report_path, parent_folder_id, parent_id) values (NOW(), NOW(), 'Batch Size Throughput', 'Per_Batchsize', (select id from report where default_folder_path = "Throughput_Reports"), null);
insert into sub_report (creation_date, last_modified, report_name, default_report_path, parent_folder_id, parent_id) values (NOW(), NOW(), 'Week Days Throughput', 'Per_Weekdays', (select id from report where default_folder_path = "Throughput_Reports"), null);
insert into sub_report (creation_date, last_modified, report_name, default_report_path, parent_folder_id, parent_id) values (NOW(), NOW(), 'Hourly Throughput', 'Per_Hourly', (select id from report where default_folder_path = "Throughput_Reports"), null);
insert into sub_report (creation_date, last_modified, report_name, default_report_path, parent_folder_id, parent_id) values (NOW(), NOW(), 'Weekly Throughput', 'Per_Weekly', (select id from report where default_folder_path = "Throughput_Reports"), null);
insert into sub_report (creation_date, last_modified, report_name, default_report_path, parent_folder_id, parent_id) values (NOW(), NOW(), 'Monthly Throughput', 'Per_Monthly', (select id from report where default_folder_path = "Throughput_Reports"), null);
insert into sub_report (creation_date, last_modified, report_name, default_report_path, parent_folder_id, parent_id) values (NOW(), NOW(), 'Module Throughput', 'Per_Module', (select id from report where default_folder_path = "Throughput_Reports"), null);
insert into sub_report (creation_date, last_modified, report_name, default_report_path, parent_folder_id, parent_id) values (NOW(), NOW(), 'User Throughput', 'Processing_Per_User', (select id from report where default_folder_path = "Throughput_Reports"), null);
insert into sub_report (creation_date, last_modified, report_name, default_report_path, parent_folder_id, parent_id) values (NOW(), NOW(), 'Document Correction', 'Document_Correction_Report', (select id from report where default_folder_path = "Advanced_Reports"), null);
insert into sub_report (creation_date, last_modified, report_name, default_report_path, parent_folder_id, parent_id) values (NOW(), NOW(), 'Extraction Correction', 'Extraction_Correction_Report', (select id from report where default_folder_path = "Advanced_Reports"), null);
insert into sub_report (creation_date, last_modified, report_name, default_report_path, parent_folder_id, parent_id) values (NOW(), NOW(), 'Field Correction', 'Field_Correction_Report', (select id from report where default_folder_path = "Advanced_Reports"), (select id from sub_report sr where default_report_path = "Extraction_Correction_Report"));
insert into sub_report (creation_date, last_modified, report_name, default_report_path, parent_folder_id, parent_id) values (NOW(), NOW(), 'Field Correction Detail', 'Field_Correction_Detail_Report', (select id from report where default_folder_path = "Advanced_Reports"), (select id from sub_report sr where default_report_path = "Field_Correction_Report"));
insert into sub_report (creation_date, last_modified, report_name, default_report_path, parent_folder_id, parent_id) values (NOW(), NOW(), 'Classification Accuracy', 'Classification_Accuracy_Report', (select id from report where default_folder_path = "Advanced_Reports"), (select id from sub_report sr where default_report_path = "Document_Correction_Report"));
insert into sub_report (creation_date, last_modified, report_name, default_report_path, parent_folder_id, parent_id) values (NOW(), NOW(), 'Classification Correction Details', 'Classification_Correction_Report', (select id from report where default_folder_path = "Advanced_Reports"), (select id from sub_report sr where default_report_path = "Classification_Accuracy_Report"));
insert into sub_report (creation_date, last_modified, report_name, default_report_path, parent_folder_id, parent_id) values (NOW(), NOW(), 'Separation Accuracy', 'Separation_Accuracy_Report', (select id from report where default_folder_path = "Advanced_Reports"), (select id from sub_report sr where default_report_path = "Document_Correction_Report"));
insert into sub_report (creation_date, last_modified, report_name, default_report_path, parent_folder_id, parent_id) values (NOW(), NOW(), 'Separation Correction Details', 'Separation_Correction_Report', (select id from report where default_folder_path = "Advanced_Reports"), (select id from sub_report sr where default_report_path = "Separation_Accuracy_Report"));
insert into sub_report (creation_date, last_modified, report_name, default_report_path, parent_folder_id, parent_id) values (NOW(), NOW(), 'False Positive', 'False_Positive_Report', (select id from report where default_folder_path = "Advanced_Reports"), (select id from sub_report sr where default_report_path = "Document_Correction_Report"));
insert into sub_report (creation_date, last_modified, report_name, default_report_path, parent_folder_id, parent_id) values (NOW(), NOW(), 'Unnecessary Review', 'Unnecessary_Review_Report', (select id from report where default_folder_path = "Advanced_Reports"), (select id from sub_report sr where default_report_path = "Document_Correction_Report"));
insert into sub_report (creation_date, last_modified, report_name, default_report_path, parent_folder_id, parent_id) values (NOW(), NOW(), 'Document Correction Analysis', 'Document_Correction_Analysis_Grid', (select id from report where default_folder_path = "Analysis_Grid"), null);
insert into sub_report (creation_date, last_modified, report_name, default_report_path, parent_folder_id, parent_id) values (NOW(), NOW(), 'Field Correction Analysis', 'Field_Correction_Analysis_Grid', (select id from report where default_folder_path = "Analysis_Grid"), null);

/*Updating plugin config. Removing extra plugin properties from plugin config.*/
UPDATE plugin_config SET is_deleted=b'1' WHERE  config_name='dbExport.database.userName';
UPDATE plugin_config SET is_deleted=b'1' WHERE  config_name='dbExport.database.password';
UPDATE plugin_config SET is_deleted=b'1' WHERE  config_name='dbExport.database.driver';
UPDATE plugin_config SET is_deleted=b'1' WHERE  config_name='dbExport.database.connectionUrl';


/*Updating plugin config. Removing extra plugin properties from plugin config.*/
UPDATE plugin_config SET is_deleted=b'1' WHERE  config_name='fuzzydb.database.userName';
UPDATE plugin_config SET is_deleted=b'1' WHERE  config_name='fuzzydb.database.password';
UPDATE plugin_config SET is_deleted=b'1' WHERE  config_name='fuzzydb.database.driver';
UPDATE plugin_config SET is_deleted=b'1' WHERE  config_name='fuzzydb.database.connectionURL';

/*Update plugin. Soft deleting plugins from plugin table*/
UPDATE plugin SET is_deleted=b'1' WHERE  plugin_name='FILEBOUND_EXPORT';
UPDATE plugin SET is_deleted=b'1' WHERE  plugin_name='HTML_TO_XML';
UPDATE plugin SET is_deleted=b'1' WHERE  plugin_name='NSI_EXPORT';
UPDATE plugin SET is_deleted=b'1' WHERE  plugin_name='OCROPUS';
UPDATE plugin SET is_deleted=b'1' WHERE  plugin_name='SCRIPTING_PLUGIN';

update field_type set field_widget_type = "COMBO" where (field_option_value_list is null or field_option_value_list = '');

/*Renaming categorise property value from NULL to 'Group 1'*/
update field_type set field_category_name = 'Group 1' where field_category_name is NULL;

/*Updating for all BC*/
/*Renaming Multipage export folder path in Copy batch xml module*/
update batch_class_plugin_config set batch_class_plugin_config.plugin_config_value='@@INSTALL_DIR_SQL@@SharedFolders/final-drop-folder/$BATCH_CLASS/$BATCH_IDENTIFIER/$DATE(yyyy-MM-dd)' where batch_class_plugin_config.plugin_config_id in (select plugin_config.id from plugin_config where plugin_config.config_name='batch.multipage_export_folder_path');


/*Adding queries for changing the size of columns to accomodate multiple regex patterns in one*/
alter table field_type modify column pattern TEXT;
alter table kv_extraction modify column key_pattern TEXT;
alter table kv_extraction modify column value_pattern TEXT;
alter table regex_validation modify column pattern TEXT;
alter table table_info modify column start_pattern TEXT;
alter table table_info modify column end_pattern TEXT;
alter table table_extraction_rule modify column start_pattern TEXT;
alter table table_extraction_rule modify column end_pattern TEXT;
alter table table_column_extraction_rule modify column between_left TEXT;
alter table table_column_extraction_rule modify column between_right TEXT;
alter table table_column_extraction_rule modify column column_pattern TEXT;
alter table table_column_extraction_rule modify column column_header_pattern TEXT;
alter table batch_class_field modify column validation_pattern TEXT;
alter table kv_page_process modify column key_pattern TEXT;
alter table kv_page_process modify column value_pattern TEXT;

/*Updating modules*/
UPDATE module SET module_display_name='Page Process Module' WHERE module_name='Page Process';
UPDATE module SET module_display_name='Folder Import Module' WHERE module_name='Folder Import';
UPDATE module SET module_display_name='Document Assembly Module' WHERE module_name='Document Assembly';
UPDATE module SET module_display_name='Extraction Module' WHERE module_name='Extraction';
UPDATE module SET module_display_name='Review Document Module' WHERE module_name='Review Document';
UPDATE module SET module_display_name='Validate Document Module' WHERE module_name='Validate Document';
UPDATE module SET module_display_name='Export Module' WHERE module_name='Export';
UPDATE module SET module_display_name='Automated Validation Module' WHERE module_name='Automated Validation';

/*Community Version*/
/*Removed three more plugins.*/
update plugin set is_deleted = 1 where plugin_name in ("SCRIPTING_PLUGIN","RECOSTAR_HOCR","FUZZYDB","FILEBOUND_EXPORT","RECOSTAR_EXTRACTION","DOCUSHARE_EXPORT","NSI_EXPORT","CSV_FILE_CREATION_PLUGIN","IBM_CM_PLUGIN","TABBED_PDF","KEY_VALUE_LEARNING_PLUGIN","DB_EXPORT","EXPORT_SCRIPTING_PLUGIN","DOCUMENT_ASSEMBLER_SCRIPTING_PLUGIN","AUTOMATED_VALIDATION_SCRIPTING_PLUGIN","PAGE_PROCESS_SCRIPTING_PLUGIN","EXPORT_TO_HPII_FILENET","NUANCE_HOCR","NUANCE_EXTRACTION_PLUGIN", "EXTRACTION_SCRIPTING_PLUGIN","CMIS_EXPORT","REVIEW_DOCUMENT");

update batch_class_plugin_config set batch_class_plugin_config.plugin_config_value='OFF' where batch_class_plugin_config.plugin_config_id in (select plugin_config.id from plugin_config where plugin_config.config_name='da.da_advanced_algorithm');
update batch_class_plugin_config set batch_class_plugin_config.plugin_config_value='OFF' where batch_class_plugin_config.plugin_config_id in (select plugin_config.id from plugin_config where plugin_config.config_name='validation.validationScriptSwitch');
update batch_class_plugin_config set batch_class_plugin_config.plugin_config_value='OFF' where batch_class_plugin_config.plugin_config_id in (select plugin_config.id from plugin_config where plugin_config.config_name='validation.fuzzy_search_switch');
update batch_class_plugin_config set batch_class_plugin_config.plugin_config_value='Ghostscript' where batch_class_plugin_config.plugin_config_id in (select plugin_config.id from plugin_config where plugin_config.config_name='folderimporter.pdf_to_tiff_process');

/*Updating plugin config. Removing validate_plugin config values*/
UPDATE plugin_config SET is_deleted=b'1' WHERE config_name in ('validation.external_app_switch','validation.field_value_change_script_switch','validation.fuzzy_search_switch','validation.fuzzy_search_pop_up_x_dimension','validation.fuzzy_search_pop_up_y_dimension','validation.insert_table_row_script_switch','validation.validationScriptSwitch','validation.url(Ctrl+4)','validation.url1_title','validation.url(Ctrl+7)','validation.url2_title','validation.url(Ctrl+8)','validation.url3_title','validation.url(Ctrl+9)','validation.url4_title','validation.x_dimension','validation.y_dimension');

/* Update Copy Batch XML Plugin Configuration.*/
UPDATE plugin_config SET is_deleted=b'1' WHERE  config_name='batch.folder_name';
UPDATE plugin_config SET is_deleted=b'1' WHERE  config_name='batch.file_name';
UPDATE plugin_config SET config_desc='Batch XML Export Folder Location' WHERE  config_name='batch.export_to_folder';
UPDATE plugin_config SET config_desc='Export Document Folder Location' WHERE  config_name='batch.multipage_export_folder_path';
UPDATE plugin_config SET config_desc='Export Document File Name' WHERE  config_name='batch.multipage_export_file_name';
UPDATE plugin_config SET config_desc='Copy File Type(s)' WHERE  config_name='batch.copy_file_type';

/*Updating CMIS Export File Name to $BATCH_IDENTIFIER*/
update batch_class_plugin_config set plugin_config_value = '$BATCH_IDENTIFIER' where plugin_config_id = (select id from plugin_config where config_name = 'cmis.file_name');