﻿/**
 * ******************************* Default locale
 * **************************************************
 */
var uploadBatchConstants = {
		tabLabel_home: "Home",
		tabLabel_batch_detail: "Batch Detail",
		tabLabel_web_scanner: "Web Scanner",
		tabLabel_upload_batch: "Upload Batch",
		associate_bcf_button: "Field(s)",
		bcf_name: "Name",
		bcf_value: "Value",
		upload_text: "Select image(s) to upload",
		upload_button_label: "Upload Image(s)",
		reset: "Reset",
		finish_button_label : "Start Batch",
		ok: "OK",
		cancel: "Cancel",
		delete_button_label : "Delete",
		select_all : "Select all",
		file_types : "tiff, tif and pdf files",
		upload_progress : "Uploaded: ",
		batch_class: "Batch Class",
		unable_to_upload_error : "Unable to upload",
		upload_batch_title:"Upload Batch",
		start_batch:"Start Batch",
		delete_files:"Delete File(s)",
		fields : "Field(s)",
		reset_all : "Reset All",
		batch_class : "Batch Class: ",
		description : "Description: ",
		unable_to_start_batch_please_select_a_valid_batch_class : "Unable to start batch. Please select a valid Batch Class.",
		unable_to_start_batch_please_enter_batch_description : "Unable to start batch. Please enter Batch Description.",
		upload_speed_in_KBs : "Upload Speed in KB/s",
		upload_details : "Upload Details",
		file_name : "File Name",
		size : "Size",
		uploaded : "Uploaded",
		elapsed : "Elapsed",
		current_speed : "Current Speed",
		average_speed : "Average Speed",
		slash_s : "/s",
		secs : " secs",
		upload_speed_in : "Upload Speed in ",
		upload_details : "Upload Details",
		upload_files : "Upload Files",
		batch_class_fields : "Batch Class Field(s)",
		please_wait :"Please wait...",
		queued_for_processing : "'  has been queued up for processing.",
		batch : "Batch '"
};

var uploadBatchMesseges = {
	upload_file_invalid_type: "Please select a valid upload file. Supported formats are tif, tiff or pdf.",
	upload_unsuccessful : "Error occurred while uploading some of the files. See server logs for more details.",
	none_selected_warning : "No Batch class selected",
	error_retrieving_bcf : "Error in retrieving batch class field(s)",
	blank_error : "Mandatory fields cannot be blank.",
	invalid_regex_pattern : "Invalid regex pattern",
	waiting_message : "Please wait....Associating Field(s)",
	associate_batch_class_field_success : "Field(s) associated successfully",
	no_batch_class_field_found : "No Batch Class Field Found",
	error_saving_bcf : "Error in saving batch class field(s)",
	bcf_validation_regex_message : "Field value should be compliant with the pattern: ",
	batch_process_fail : "Could not start batch processing.",
	finish_upload_alert : "Your batch has been queued up for processing.",
	error_finish_clicked : "Please select at least a file for upload",
	file_upload_complete_alert : "File(s) uploaded successfully.",
	back_without_finish_upload : "Are you sure you want to leave upload batch?",
	error_deleting_bcf : "Error deleting the last associated Batch Class Field. Please try again.",
	error_deleting_files : "Error deleting the files at server. Please try again.",
	few_files_not_deleted : "Error deleting few files at server. Please try again.",
	success_deleting_files : "File(s) deleted successfully.",
	none_file_to_delete_selected_warning : "No files selected to delete.Please select at least one file",
	upload_image_limit_message : "Maximum permissible upload limit is ",
	upload_instance_limit_message : "Maximum batch processing limit has been reached.",
	limit_reached : "Limit Reached",
	file_size_exceed_meesage : " file(s) exceeded permissible file size limit of",
	upload_instance_limit_appended_message : "files. One or more uploaded files may contain more than 1 images.",
	
	unable_to_fetch_current_upload_folder_name : "Unable to fetch current Upload Folder Name.",
	fetching_batch_classes_please_wait : "Fetching Batch Classes. Please wait...",
	
	unable_to_get_batch_class_list : "Unable to get Batch Class List.",
		
		no_files_selected_to_delete :"No files selected to delete.",
		
		are_you_sure_you_want_to_delete_these_files : "Are you sure you want to delete these files?",
		
		unable_to_delete_selected_files_please_try_again : "Unable to delete selected files. Please try Again",
		
		no_files_uploaded : "No files Uploaded.",
		
		no_files_uploaded_to_start_batch : "No files Uploaded to start Batch.",
		
		unable_to_fetch_batch_class_fields_for : "Unable to fetch Batch Class Fields for ",
		
		serializing_batch_class_fields : "Serializing Batch Class Fields.",
		
		unable_to_serialize_batch_class_fields : "Unable to serialize batch class fields.",
		
		Unable_to_upload_batch_please_try_again : "Unable to Upload Batch. Please try Again",
		
		uploading_batch : "Uploading Batch.",
		
		success : "Success",
		
		unable_to_upload : "Unable to upload ",
			
		files_unsupported_format : " file(s). Unsupported format.",
		
		show_file_list : "Show File List",
		
		incorrect_value_for_batch_class_field : "Incorrect value for Batch Class Field",
		
		office_file_support_message: "Support for Office files is present in Enterprise version."
};

/** ************************** Turkish locale (suffix: _tk)****************** */
var uploadBatchConstants_tr = {
		tabLabel_home : "Ev",
		tabLabel_batch_detail : "Toplu Detay",
		tabLabel_web_scanner : "Web Tarayıcı",
		tabLabel_upload_batch: "Toplu Yükle",
		associate_bcf_button: "Alan (lar)",
		bcf_name: "Isim",
		bcf_value: "Değer",
		upload_text: "Yüklemek için resim seçin (ler)",
		upload_button_label: "Yükle Görüntü (ler)",
		reset: "Sıfırla",
		finish_button_label : "Toplu Başlat",
		ok: "Tamam",
		cancel: "iptal",
		delete_button_label : "silmek",
		select_all : "Hepsini Seç",
		file_types : "tiff, tif ve pdf dosyalarını",
		upload_progress : "Gönderildi: ",
		batch_class: "Toplu Sınıf",
		unable_to_upload_error : "Yüklenemiyor",
		upload_batch_title : "Toplu yükle",
		
		start_batch:"Toplu başlat",
		delete_files:"Dosyayı Sil (ler)",
		fields : "Alan (lar)",
		reset_all : "Tümünü sıfırla",
		batch_class : "Toplu Sınıfı : ",
		description : "tanım: ",
		unable_to_start_batch_please_select_a_valid_batch_class : "Toplu başlatılamıyor . Geçerli bir Toplu Sınıf seçiniz .",
		unable_to_start_batch_please_enter_batch_description : "Toplu başlatılamıyor . Toplu Açıklama giriniz .",
		upload_speed_in_KBs : "KB / s Hız yükle",
		upload_details : "Yükleme Detayları",
		file_name : "dosya Adı",
		size : "boyut",
		uploaded : "yüklendi",
		elapsed : "geçen",
		current_speed : "Güncel Hız",
		average_speed : "Ortalama Hız",
		slash_s : "/ sn",
		secs : " kuru",
		upload_speed_in : "Hızının yükle",
		upload_details : "Yükleme Detayları",
		upload_files : "Dosya Yükle",
		batch_class_fields : "Toplu Sınıf Alan (lar )",
		please_wait :"Lütfen bekleyin ...",
		queued_for_processing : "' Işleme sıraya edilmiştir .",
		batch : "toplu '"
};

var uploadBatchMesseges_tr = {		
		upload_file_invalid_type: "Etiketler: Lütfen seçin geçerli bir yükleme file.Supported formatları tif, tiff veya pdf.",
		upload_unsuccessful : "Hata baz? dosyalar? yükleyerek olu?tu. Daha fazla bilgi için sunucu günlüklerine bak?n.",
		none_selected_warning: "Toplu sınıf seçilen",
		error_retrieving_bcf : "Hata almak toplu sınıf alan (lar)",
		blank_error : "Zorunlu alanlar boş bırakılamaz.",
		invalid_regex_pattern : "Invalid regex pattern",
		waiting_message : "Lütfen bekleyin .... ilişkilendirilmesi Alan (lar)",
		associate_batch_class_field_success : "Alan (lar) başarılı biçimde ilişkilendirilse",
		no_batch_class_field_found : "Toplu Sınıf Alan Bulunamadı",
		error_saving_bcf : "Hata tasarrufu toplu sınıf alan (lar)",
		bcf_validation_regex_message : "Alan değeri desen uyumlu olmalıdır: ",
		batch_process_fail : "Toplu işlem başlatılamadı.",
		finish_upload_alert : "Toplu işlem için kuyruğa sahiptir.",
		error_finish_clicked : "Lütfen yüklemek için en az bir dosya seçin",
		file_upload_complete_alert : "Dosya (lar) başarıyla yüklendi.",
		back_without_finish_upload : "Yükleme toplu terk etmek istediğinizden emin mısınız?",
		error_deleting_bcf: "Son ilişkili Toplu Sınıf Alan silme hatası. Lütfen yeniden deneyin.",
		error_deleting_files: "Sunucuda dosyaları silerek hata. Lütfen yeniden deneyin.",
		few_files_not_deleted: "Sunucuda birkaç dosya silme hatası. Lütfen yeniden deneyin.",
		success_deleting_files: "Dosya (lar) başarıyla silindi.",
		none_file_to_delete_selected_warning: "Delete.Please için seçilen hiçbir dosya en az bir dosya seçin",
		upload_image_limit_message: "İzin verilen maksimum yükleme sınırı",
		upload_instance_limit_message: "Maksimum toplu işlem sınırına ulaşıldı.",
		limit_reached : "Ulaşıldı sınırlayın",
		file_size_exceed_meesage : " dosya (lar) aşıldı müsaade dosya boyutu limiti",
		upload_instance_limit_appended_message : "dosyaları. Bir veya daha fazla yüklenen dosyalar en fazla 1 görüntüler içerebilir.",
		
		unable_to_fetch_current_upload_folder_name : "Mevcut yükle Klasör Adı alınamıyor .",
		fetching_batch_classes_please_wait : "Alınıyor Toplu Sınıflar . Lütfen bekleyin ...",
		
		unable_to_get_batch_class_list : "Toplu Sınıf Listesini alınamıyor .",
			
			no_files_selected_to_delete :"Hiçbir dosya silmek için seçilmiş .",
			
			are_you_sure_you_want_to_delete_these_files : "Bu dosyaları silmek istediğinizden emin misiniz ?",
			
			unable_to_delete_selected_files_please_try_again : "Seçilen dosyaları silmek için açılamıyor . Yine deneyin",
			
			no_files_uploaded : "Hiçbir dosya yüklendi .",
			
			no_files_uploaded_to_start_batch : "Hiçbir dosya Toplu başlatmak için yüklendi .",
			
			unable_to_fetch_batch_class_fields_for : "Için Toplu Sınıf alanları alınamıyor",
			
			serializing_batch_class_fields : "Dizgeleştirme Toplu Sınıf Alanları .",
			
			unable_to_serialize_batch_class_fields : "Toplu sınıf alanları seri açılamıyor .",
			
			Unable_to_upload_batch_please_try_again : "Toplu yükle yapılamıyor . Yine deneyin",
			
			uploading_batch : "Yükleme Toplu .",
			
			success : "başarı",
			
			unable_to_upload : "Yüklenemiyor",
				
			files_unsupported_format : " Dosya ( lar ) . Desteklenmeyen biçim .",
			
			show_file_list : "Göster Dosya Listesi",
			
			incorrect_value_for_batch_class_field : "Toplu Sınıf Field için yanlış değer",
			
			office_file_support_message: "Ofis dosyaları için destek Kurumsal sürüm mevcut."
				
				
};

/**
 * ******************************* French language
 * ******************************* Grégory CARLIN, INEAT Conseil (Ephesoft french partner)
 * ******************************* v1.0
 * **************************************************
 */
var uploadBatchConstants_fr = {
		tabLabel_home: "Accueil",
		tabLabel_batch_detail: "Détail du lot",
		tabLabel_web_scanner: "Web Scanner",
		tabLabel_upload_batch: "Envoyer un lot",
		associate_bcf_button: "Champs",
		bcf_name: "Nom",
		bcf_value: "Valeur",
		upload_text: "Sélectionnez la/les image(s) à envoyer",
		upload_label: "<b><font color=\"black\">Envoyer la/les image(s)</font></b>",
		upload_button_label: "Envoyer la/les image(s)",
		reset: "Réinitialiser",
		finish_button_label : "Commencer le lot",
		ok: "OK",
		cancel: "Annuler",
		delete_button_label : "Effacer",
		select_all : "Tout sélectionner",
		batch_detail : "<b><font color=\"black\">Détail du lot</font></b>",
		action : "<b><font color=\"black\">Action</font></b>",
		file_list : "<b><font color=\"black\">Liste de fichiers</font></b>",
		file_types : "tiff, tif et fichiers pdf",
		upload_progress : "Envoyé: ",
		batch_class: "Classe de lots",
		upload_batch_title :"Télécharger Batch",
		start_batch:"Lancer Batch",
		delete_files:"Supprimer le fichier (s )",
		fields : "Domaine (s)",
		reset_all : "Tout réinitialiser",
		batch_class : "Lot Classe: ",
		description : "Description: ",
		unable_to_start_batch_please_select_a_valid_batch_class : "Impossible de démarrer lot. Se il vous plaît sélectionner un lot de classe valide.",
		unable_to_start_batch_please_enter_batch_description : "mpossible de démarrer lot. Se il vous plaît entrer Batch description .",
		upload_speed_in_KBs : "Vitesse de téléchargement en Ko / s",
		upload_details : "Télécharger Détails",
		file_name : "Nom de fichier",
		size : "taille",
		uploaded : "Téléchargé",
		elapsed : "passé",
		current_speed : "Vitesse actuelle",
		average_speed : "Vitesse moyenne",
		slash_s : "/s",
		secs : " secs",
		upload_speed_in : "Vitesse de téléchargement en",
		upload_details : "Télécharger Détails",
		upload_files : "Télécharger des fichiers",
		batch_class_fields : "Batch classe Domaine (s)",
		please_wait :"Se il vous plaît patienter ...",
		queued_for_processing : "»A été mis en attente pour le traitement .",
		batch : "lot »"
};

var uploadBatchMesseges_fr = {
		upload_file_invalid_type: "Sélectionnez SVP un format fichier valide (tif, tiff ou pdf).",
		upload_unsuccessful : "Une erreur s'est produite lors de l'envoi du fichier ",
		none_selected_warning: "Pas de classe de lots sélectionnée",
		error_retrieving_bcf : "Erreur lors de la récupération des champs de la classe de lots",
		blank_error : "Les champs obligatoires ne peuvent pas être vides.",
		invalid_regex_pattern : "Modèle regex invalide",
		waiting_message : "Attendez SVP....Champs en cours d'association",
		associate_batch_class_field_success : "Champs associés avec succès",
		no_batch_class_field_found : "Pas de classe de lots trouvé",
		error_saving_bcf : "Erreur lors de la sauvegarde des champs de la classe de lots",
		bcf_validation_regex_message : "La valeur du champs doit respecter le modèle: ",
		batch_process_fail : "Ne peut pas démarré l'exécution du lot.",
		finish_upload_alert : "Votre lot a été mise en attente pour traitement.",
		error_finish_clicked : "Sélectionnez SVP au moins un fichier à envoyer",
		file_upload_complete_alert : "Fichier(s) envoyé(s) avec succès",
		back_without_finish_upload : "Êtes-vous sûr de vouloir quitter le lot d'envoi.",
		error_deleting_bcf: "Erreur lors de la suppression du dernier champs de la classe de lots associée. Essayez encore SVP.",
		error_deleting_files: "Erreur lors de la suppression des fichiers sur le serveur. Essayez encore SVP.",
		few_files_not_deleted: "Erreur lors de la suppression de quelques fichiers sur le serveur. Essayez encore SVP.",
		success_deleting_files: "Fichier(s) supprimé(s) avec succès.",
		none_file_to_delete_selected_warning: "Pas de fichiers sélectionné pour suppression. Sélectionnez SVP au moins un fichier",
		upload_image_limit_message: "La limite d'envoi de fichier est ",
		upload_instance_limit_message: "La limite maximum de traitement de lots a été atteinte.",
		limit_reached : "Limite atteinte",
		file_size_exceed_meesage : " taillle total de fichiers(s) dépassée de",
		upload_instance_limit_appended_message : "fichiers. Un ou plusieurs fichiers envoyés peuvent contenir plus d'une image.",
		
		unable_to_fetch_current_upload_folder_name : "Impossible de récupérer actuelle Nom chargement de dossier ",
		fetching_batch_classes_please_wait : "Classes de chargement par lot . Se il vous plaît patienter ...",
		
		unable_to_get_batch_class_list : "Impossible d'obtenir la liste lot de classe .",
			
			no_files_selected_to_delete :"Aucun sélectionnés à supprimer.",
			
			are_you_sure_you_want_to_delete_these_files : "Êtes-vous sûr de vouloir supprimer ces fichiers ?",
			
			unable_to_delete_selected_files_please_try_again : "Impossible de supprimer les fichiers sélectionnés . Se il vous plaît essayez Encore une fois",
			
			no_files_uploaded : "Pas de fichiers téléchargés .",
			
			no_files_uploaded_to_start_batch : "Pas de fichiers téléchargés pour commencer lot .",
			
			unable_to_fetch_batch_class_fields_for : "Impossible de récupérer le lot Champs de classe pour",
			
			serializing_batch_class_fields : "Batch sérialisation Champs de classe .",
			
			unable_to_serialize_batch_class_fields : "mpossible de sérialiser champs de classe du lot .",
			
			Unable_to_upload_batch_please_try_again : "Impossible de télécharger Batch . Se il vous plaît essayez Encore une fois",
			
			uploading_batch : "Batch ajout .",
			
			success : "succès",
			
			unable_to_upload : "Impossible de télécharger ",
				
			files_unsupported_format : " fichier ( s) . Format non supporté .",
			
			show_file_list : "Afficher la liste de fichiers",
			
			incorrect_value_for_batch_class_field : "Valeur incorrecte pour Batch Classe terrain",
			
			office_file_support_message: "Support pour les fichiers Office est présent dans la version Enterprise."
};

/**
 * 
 * ******************************* Spanish locale **************************************************
 */
var uploadBatchConstants_es = {
		tabLabel_home: "Casa",
		tabLabel_batch_detail: "Detalle de lotes",
		tabLabel_web_scanner: "Web Scanner",
		tabLabel_upload_batch: "Carga por lotes",
		associate_bcf_button: "Campo (s)",
		bcf_name: "Nombre",
		bcf_value: "Valor",
		upload_text: "Seleccionar imagen (s) para cargar",
		upload_button_label: "Subir imagen (s)",
		reset: "Reajustar",
		finish_button_label : "Comience por lotes",
		ok: "Okay",
		cancel: "Cancelar",
		delete_button_label : "Borrar",
		select_all : "Seleccionar todo",
		file_types : "tiff, tif y archivos pdf",
		upload_progress : "Subido: ",
		batch_class: "Clase de lote",
		unable_to_upload_error : "No se puede subir",
		upload_batch_title:"Carga por lotes",
		start_batch:"Comience por lotes",
		delete_files:"Eliminar archivo (s)",
		fields : "Campo (s)",
		reset_all : "Reiniciar todo",
		batch_class : "Clase de lote: ",
		description : "Descripción: ",
		unable_to_start_batch_please_select_a_valid_batch_class : "No se puede iniciar por lotes. Por favor seleccione una clase de lote válida.",
		unable_to_start_batch_please_enter_batch_description : "No se puede iniciar por lotes. Por favor ingrese Descripción Lote.",
		upload_speed_in_KBs : "Sube velocidad en KB / s",
		upload_details : "Subir Detalles",
		file_name : "Nombre Del Archivo",
		size : "Tamaño",
		uploaded : "Subido",
		elapsed : "Transcurrido",
		current_speed : "Velocidad actual",
		average_speed : "Velocidad media",
		slash_s : "/ S",
		secs : "segs",
		upload_speed_in : "Sube velocidad en ",
		upload_details : "Subir Detalles",
		upload_files : "Cargar archivos",
		batch_class_fields : "Clase Field (s) de lote",
		please_wait :"Por favor espera ...",
		queued_for_processing : "'Ha puesto en cola para su procesamiento.",
		batch : "Lote '"
};

var uploadBatchMesseges_es = {
		upload_file_invalid_type: "Por favor, seleccione un archivo cargado válido. Los formatos soportados son tif, tiff o pdf.",
		upload_unsuccessful : "Se produjo un error al cargar algunos de los archivos. Ver los registros del servidor para obtener más detalles.",
		none_selected_warning : "Ninguna clase de lote seleccionado",
		error_retrieving_bcf : "Error en la recuperación de campo clase de lote (s)",
		blank_error : "Los campos obligatorios no pueden estar en blanco.",
		invalid_regex_pattern : "Patrón de expresión no válida",
		waiting_message : "Por favor espera ....Asociar Field (s)",
		associate_batch_class_field_success : "Campo (s) asociado con éxito",
		no_batch_class_field_found : "No lotes Clase Campo encontrado",
		error_saving_bcf : "Error en el ahorro de campo clase de lote (s)",
		bcf_validation_regex_message : "El valor del campo debe ser compatible con el patrón: ",
		batch_process_fail : "No se pudo iniciar el procesamiento por lotes.",
		finish_upload_alert : "Su lote está en cola para su procesamiento.",
		error_finish_clicked : "Por favor, seleccione al menos un archivo para la carga",
		file_upload_complete_alert : "Archivo (s) subido correctamente.",
		back_without_finish_upload : "¿Seguro que quieres salir de carga por lotes?",
		error_deleting_bcf : "Error al eliminar el último lote asociado Clase campo. Por favor, vuelva a intentarlo.",
		error_deleting_files : "Error al borrar los archivos en el servidor. Por favor, vuelva a intentarlo.",
		few_files_not_deleted : "Error al borrar algunos archivos en el servidor. Por favor, vuelva a intentarlo.",
		success_deleting_files : "Archivo (s) eliminado correctamente.",
		none_file_to_delete_selected_warning : "No hay archivos seleccionados para eliminar.Por favor, seleccione al menos un archivo",
		upload_image_limit_message : "Límite máximo de carga admisible es de ",
		upload_instance_limit_message : "Se ha alcanzado el límite máximo de procesamiento por lotes.",
		limit_reached : "Límite alcanzado",
		file_size_exceed_meesage : "archivo (s) excede el límite de tamaño de archivo permitido de",
		upload_instance_limit_appended_message : "archivos. Uno o más archivos cargados pueden contener más de 1 imágenes.",
		unable_to_fetch_current_upload_folder_name : "Imposible obtener actual Nombre Subir carpeta.",
		fetching_batch_classes_please_wait : "Clases recuperación en lotes. Por favor espera ...",
		unable_to_get_batch_class_list : "No se puede obtener listas de clases de lotes.",
		no_files_selected_to_delete :"No hay archivos seleccionados para eliminar.",
		are_you_sure_you_want_to_delete_these_files : "¿Seguro que quieres eliminar estos archivos?",
		unable_to_delete_selected_files_please_try_again : "No se puede eliminar los archivos seleccionados. Por favor, inténtalo de nuevo",
		no_files_uploaded : "No hay archivos subidos.",
		no_files_uploaded_to_start_batch : "No hay archivos subidos a empezar por lotes.",
		unable_to_fetch_batch_class_fields_for : "Imposible obtener Clase campos lotes para ",
		serializing_batch_class_fields : "Lotes de números de serie Clase campos.",
		unable_to_serialize_batch_class_fields : "No puede serializar campos de clase de lote.",
		Unable_to_upload_batch_please_try_again : "No se puede subir por lotes. Por favor, inténtalo de nuevo",
		uploading_batch : "Carga por lotes.",
		success : "Éxito",
		unable_to_upload : "No se puede subir ",
		files_unsupported_format : "archivo (s). Formato no compatible.",
		show_file_list : "Mostrar lista de archivos",
		incorrect_value_for_batch_class_field : "Valor incorrecto para lotes Clase Campo",
		office_file_support_message: "Soporte para archivos de Office está presente en la versión Enterprise."
};
