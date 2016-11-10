/********************************************************************************* 
* Ephesoft is a Intelligent Document Capture and Mailroom Automation program 
* developed by Ephesoft, Inc. Copyright (C) 2015 Ephesoft Inc. 
* 
* This program is free software; you can redistribute it and/or modify it under 
* the terms of the GNU Affero General Public License version 3 as published by the 
* Free Software Foundation with the addition of the following permission added 
* to Section 15 as permitted in Section 7(a): FOR ANY PART OF THE COVERED WORK 
* IN WHICH THE COPYRIGHT IS OWNED BY EPHESOFT, EPHESOFT DISCLAIMS THE WARRANTY 
* OF NON INFRINGEMENT OF THIRD PARTY RIGHTS. 
* 
* This program is distributed in the hope that it will be useful, but WITHOUT 
* ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS 
* FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more 
* details. 
* 
* You should have received a copy of the GNU Affero General Public License along with 
* this program; if not, see http://www.gnu.org/licenses or write to the Free 
* Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 
* 02110-1301 USA. 
* 
* You can contact Ephesoft, Inc. headquarters at 111 Academy Way, 
* Irvine, CA 92617, USA. or at email address info@ephesoft.com. 
* 
* The interactive user interfaces in modified source and object code versions 
* of this program must display Appropriate Legal Notices, as required under 
* Section 5 of the GNU Affero General Public License version 3. 
* 
* In accordance with Section 7(b) of the GNU Affero General Public License version 3, 
* these Appropriate Legal Notices must retain the display of the "Ephesoft" logo. 
* If the display of the logo is not reasonably feasible for 
* technical reasons, the Appropriate Legal Notices must display the words 
* "Powered by Ephesoft". 
********************************************************************************/ 

package com.ephesoft.gxt.core.client.i18n;

import com.ephesoft.gxt.core.client.DCMAEntryPoint.EphesoftUIContext;

/**
 * The <code>CoreCommonConstants</code> is an interface for constants common to whole application UI.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.gwt.core.client.i18n
 * 
 */
public interface CoreCommonConstants {

	public static final String FOOTER_TEXT_KEY = "footer_text_key";

	public static final String FOOTER_LINK_KEY = "footer_link_key";

	String UNDERSCORE = "_";
	/**
	 * The BUTTON_MARGIN_RIGHT_CSS {@link String} is a common CSS for right margin for button with CSS class named 'btnMrgRight'.
	 */
	String BUTTON_MARGIN_RIGHT_CSS = "btnMrgRight";

	String EMPTY_STRING = "";

	/**
	 * The BOLD_TEXT_CSS {@link String} is a common CSS for bold text with CSS class named 'boldTxt'.
	 */
	String BOLD_TEXT_CSS = "boldTxt";

	/**
	 * The HEADER_BOLD_TEXT_CSS {@link String} is a common CSS for header text with CSS class named 'headTxt'.
	 */
	String HEADER_BOLD_TEXT_CSS = "headTxt";

	/**
	 * The TOP_PANEL_CSS {@link String} is a common CSS for top status panel with CSS class named 'topPanel'.
	 */
	String TOP_PANEL_CSS = "topPanel";

	/**
	 * The BUTTON_CONTAINER_CSS {@link String} is a common CSS for base container for buttons with CSS class named 'buttonContainer'.
	 */
	String BUTTON_CONTAINER_CSS = "buttonContainer";

	/**
	 * The GWT_SMALL_BUTTON_CSS {@link String} is a common CSS for small buttons in application with CSS class named
	 * 'gwt-small-button'.
	 */
	String GWT_SMALL_BUTTON_CSS = "gwt-small-button";

	/**
	 * The OPTIONS_PANEL {@link String} is a common CSS for filter and search options with CSS class named 'optionsPanel'.
	 */
	String OPTIONS_PANEL = "optionsPanel";

	/**
	 * The MARGIN_BOTTOM_5_CSS {@link String} is a common CSS for margin from bottom with CSS class named 'marBottom5'.
	 */
	String MARGIN_BOTTOM_5_CSS = "marBottom5";

	/**
	 * The MARGIN_LEFT_5_CSS {@link String} is a common CSS for margin from left with CSS class named 'marLeft5'.
	 */
	String MARGIN_LEFT_5_CSS = "marLeft5";

	/**
	 * The LAST_GRP_PANEL_CSS {@link String} is a common CSS for last logical group on filter and search panels with CSS class named
	 * 'lastGroupPanelLayout'.
	 */
	String LAST_GRP_PANEL_CSS = "lastGroupPanelLayout";

	/**
	 * The OPTION_PANEL_MARGIN_CSS {@link String} is a common CSS for margin from bottom with CSS class named 'mrgnBottom'.
	 */
	String OPTION_PANEL_MARGIN_CSS = "mrgnBottom";

	/**
	 * The GRP_PANEL_NORMAL_CSS {@link String} is a common CSS for logical group on filter and search panels with CSS class named
	 * 'groupPanelLayoutNormal'.
	 */
	String GRP_PANEL_NORMAL_CSS = "groupPanelLayoutNormal";

	/**
	 * The LAST_GRP_PANEL_NORMAL_CSS {@link String} is a common CSS for last logical group on filter and search panels with CSS class
	 * named 'lastGroupPanelLayoutNormal'.
	 */
	String LAST_GRP_PANEL_NORMAL_CSS = "lastGroupPanelLayoutNormal";

	/**
	 * The MAIN_CONTAINER_CSS {@link String} is a common CSS for main outer container for all pages with CSS class named
	 * 'mainContainer'.
	 */
	String MAIN_CONTAINER_CSS = "mainContainer";

	/**
	 * The MAIN_PANEL_LAYOUT_CSS {@link String} is a common CSS for main content panel with CSS class named 'mainPanelLayout'.
	 */
	String MAIN_PANEL_LAYOUT_CSS = "mainPanelLayout";

	/**
	 * The PADDING_5_CSS {@link String} is a common CSS for all over padding of 5px with CSS class named 'padding5'.
	 */
	String PADDING_5_CSS = "padding5";

	/**
	 * The FONT_RED_CSS {@link String} is a common CSS for red font color with CSS class named 'fontRed'.
	 */
	String FONT_RED_CSS = "fontRed";

	/**
	 * The SELECT_ALL_LABEL_PADDING_CSS {@link String} is a common CSS for padding with CSS class named 'paddingSelectAllLabel'.
	 */
	String SELECT_ALL_LABEL_PADDING_CSS = "paddingSelectAllLabel";

	/**
	 * The SELECT_ALL_LABEL_PADDING_VERTICAL_CSS {@link String} is a common CSS for padding with CSS class named
	 * 'paddingSelectAllLabel'.
	 */
	String SELECT_ALL_LABEL_PADDING_VERTICAL_CSS = "paddingSelectAllLabel";

	/**
	 * The MARGIN_5PX {@link String} is a common CSS for margin of 5px all round with CSS class named 'margin5px'.
	 */
	String MARGIN_5PX = "margin5px";

	/**
	 * The PANEL_BACKGROUND_CSS {@link String} is a common CSS for upload batch panel background with CSS class named
	 * 'panelBackgroundCSS'.
	 */
	String PANEL_BACKGROUND_CSS = "panelBackgroundCSS";

	/**
	 * The PANEL_BORDER_CSS {@link String} is a common CSS for upload batch panel border with CSS class named 'panelBorderCSS'.
	 */
	String PANEL_BORDER_CSS = "panelBorderCSS";

	/**
	 * The PANEL_BORDER_SCROLLABLE_CSS {@link String} is a common css for setting the border css for scrollable panel.
	 */
	String PANEL_BORDER_SCROLLABLE_CSS = "panelBorderScrollableCSS";

	/**
	 * The BUTTON_MARGIN_LEF_RIGHT_CSS {@link String} is a common css for setting margins for buttons on left and right border.
	 */
	String BUTTON_MARGIN_LEF_RIGHT_CSS = "btnMrgLeftRight";

	/**
	 * The SCROLL_PANEL_HEIGHT_CSS {@link String} is a common css for setting scroll panel height respective to browsers.
	 */
	String SCROLL_PANEL_HEIGHT_CSS = "scrollPanelHeightCSS";

	/**
	 * The DOCK_PANEL_HEIGHT_CSS {@link String} is a common css for setting dock panel height respective to browsers.
	 */
	String DOCK_PANEL_HEIGHT_CSS = "dockPanelHeightCSS";

	/**
	 * The BROWSE_BUTTON_CSS {@link String} is a common css for flash browse button.
	 */
	String BROWSE_BUTTON_CSS = "browseButton";

	/**
	 * The _100_PERCENTAGE {@link String} is a common CSS for dimension 100%'.
	 */
	String _100_PERCENTAGE = "100%";

	/**
	 * The _20_PERCENTAGE {@link String} is a common CSS for dimension 20%'.
	 */
	String _20_PERCENTAGE = "20%";

	/**
	 * The _97PX {@link String} is a common CSS for dimension 97px'.
	 */
	String _97PX = "97px";

	/**
	 * The _58PX {@link String} is a common CSS for dimension 58px'.
	 */
	String _58PX = "58px";

	/**
	 * The _120PX {@link String} is a common CSS for dimension 120px'.
	 */
	String _120PX = "120px";

	/**
	 * The _90_PX {@link String} is a common CSS for setting the width of drop down list on BatchInstanceManagement screen to 90
	 * pixels'.
	 */
	String _90_PX = "90px";

	/**
	 * The REMOVE_MARGIN_CSS {@link String} is a CSS for removing the margin set in gwt-button CSS'.
	 */
	String REMOVE_MARGIN_CSS = "removeMargin";

	/**
	 * The BROWSER_HIDDEN_CSS {@link String} is a CSS for removing the margin set in gwt-button CSS'.
	 */
	String BROWSER_HIDDEN_CSS = "BrowserHidden";

	/**
	 * The BROWSER_VISIBLE_CSS {@link String} is a CSS for when the browser is visible in view.
	 */
	String BROWSER_VISIBLE_CSS = "BrowserVisible";

	/**
	 * The BROWSE_CSS {@link String} is a CSS for when browsing is being done.
	 */
	String BROWSE_CSS = "Browse";

	/**
	 * The REMOVE_PADDING_CSS {@link String} is a CSS for removing the padding set.
	 */
	String REMOVE_PADDING_CSS = "removePadding";

	/**
	 * The FILE_FIELD_CSS {@link String} is a CSS for file field.
	 */
	String FILE_FIELD_CSS = "FileField";

	/**
	 * /** The TEXT_BOX_CSS {@link String} is a common CSS for all the text boxes.
	 */
	String TEXT_BOX_CSS = "gwt-TextBox";

	/**
	 * The DATE_BOX_FORMAT_ERROR_CSS {@link String} is a CSS for wrong values set in a text box.
	 */
	String DATE_BOX_FORMAT_ERROR_CSS = "dateBoxFormatError";

	/**
	 * The RULE_FORMAT_ERROR_CSS {@link String} is a css for showing rule validation failure in rows.
	 */
	String RULE_FORMAT_ERROR_CSS = "ruleFormatError";

	/**
	 * The SCROLL_CSS {@link String} is a CSS for scroll bar in .
	 */
	String SCROLL_CSS = "scrollDisplayValues";

	/**
	 * The BUTTON_PANEL_PADDING_CSS {@link String} is a CSS for applying padding to the panel for buttons.
	 */
	String BUTTON_PANEL_PADDING_CSS = "paddingSelectAll";

	/**
	 * The TABLE_RESIZE_CSS {@link String} is a CSS for applying horizontal resize feature to FlexTable.
	 */
	String TABLE_RESIZE_CSS = "tableResize";

	/**
	 * The style of the cursor pointer to display column dragging or resizing.
	 */
	String RESIZABLE_CURSOR_POINTER = "resizableCursorPointer";

	/**
	 * The PIXEL is used to specify the unit on which the calculation should be done.
	 */
	String PIXEL = "px";

	/**
	 * The WIDGET_MARGIN_RIGHT_CSS {@link String} is a CSS for setting margins for the widget.
	 */
	String WIDGET_MARGIN_RIGHT_CSS = "widgetMrgRight";

	/**
	 * The LABEL_CSS {@link String} is a CSS for setting font, size and margin of the label.
	 */
	String LABEL_CSS = "labelCss";

	/**
	 * The CURSOR_HAND {@link String} is a CSS for adding pointer to the widget.
	 */
	String CURSOR_HAND = "cursorHand";

	/**
	 * The SET_SPACING_CSS {@link String} is a CSS for setting margins for the table.
	 */
	String SET_SPACING_CSS = "setSpacing";

	/**
	 * The MARGIN_LEFT_4_CSS {@link String} is a common CSS for margin from left with CSS class named 'marLeft4'.
	 */
	String MARGIN_LEFT_4_CSS = "marLeft4";

	/**
	 * The VERTICAL_SCROLL {@link String} is CSS for scroll panel for vertical scroll.
	 */
	String VERTICAL_SCROLL = "verticalScroll";

	/**
	 * The SCROLL_PANEL_HEIGHT_CSS_TROUBLESHOOT {@link String} is a common css for setting scroll panel height respective to browsers.
	 */
	String SCROLL_PANEL_HEIGHT_CSS_TROUBLESHOOT = "scrollPanelHeightCSSForTroubleshoot";

	/**
	 * Constant for CSS class name for showing label as link.
	 */
	String LABEL_LINK_CSS = "blueLink";

	/**
	 * Constant for CSS class name for setting background colour of label transparent.
	 */
	String BACKGROUND_TRANSPARENT_CSS = "backgroundTransparent";

	/**
	 * Constant for CSS class name for setting label background image for default icon.
	 */
	String DEFAULT_ICON_CSS = "defaultLabelIcon";

	/**
	 * Constant for CSS class name for setting label background image for CMIS icon.
	 */
	String CMIS_ICON_CSS = "CMISLabelIcon";

	/**
	 * Constant for CSS class name for setting label background image for document icon.
	 */
	String DOCUMENT_ICON_CSS = "documentLabelIcon";

	/**
	 * Constant for CSS class name for setting label background image for email icon.
	 */
	String EMAIL_ICON_CSS = "emailLabelIcon";

	/**
	 * Constant for CSS class name for setting label background image for extraction icon.
	 */
	String EXTRACTION_ICON_CSS = "extractionLabelIcon";

	/**
	 * Constant for CSS class name for setting label background image for field icon.
	 */
	String FIELD_ICON_CSS = "fieldLabelIcon";

	/**
	 * Constant for CSS class name for setting label background image for module icon.
	 */
	String MODULE_ICON_CSS = "moduleLabelIcon";

	/**
	 * Constant for CSS class name for setting label background image for plugin icon.
	 */
	String PLUGIN_ICON_CSS = "pluginLabelIcon";

	/**
	 * Constant for CSS class name for setting label background image for scanner icon.
	 */
	String SCANNER_ICON_CSS = "scannerLabelIcon";

	/**
	 * Constant for CSS class name for background colour and padding on a panel.
	 */
	String BREADCRUMB_PANEL_CSS = "breadcrumbPanel";

	/**
	 * SPACE_FOR_BUTTON_PANEL {@link int} sets spacing for button panel.
	 */
	int SPACE_FOR_BUTTON_PANEL = 10;

	/**
	 * SPACE_FOR_VIEW_PANEL {@link int} spacing for view panel.
	 */
	int SPACE_FOR_VIEW_PANEL = 5;

	/**
	 * Constant for CSS class name for setting text Area for table.
	 */
	String TEXT_AREA_TABLE_CSS = "textAreaTable";

	/**
	 * The MARGIN_5PX {@link String} is a common CSS for margin of 7px all round with CSS class named 'margin7px'.
	 */
	String MARGIN_7PX = "margin7px";

	/**
	 * Constant for table header widget CSS.
	 */
	String CUSTOM_GRID_HEADER_CSS = "customGridHeader";

	/**
	 * Constant for table widget odd row CSS.
	 */
	String ODD_CSS = "odd";

	/**
	 * Constant for table widget even row CSS.
	 */
	String EVEN_CSS = "even";

	/**
	 * Constant for highlighted row widget CSS.
	 */
	String ROW_HIGHLIGHTED_CSS = "rowHighlighted";

	/**
	 * Constant for table widget css.
	 */
	String CUSTOM_GRID_CSS = "customGrid";

	/**
	 * Constant for table widget css.
	 */
	String BORDER_TABLE_CSS = "borderResultTable";

	/**
	 * Constant for Css to be used for dialog box.
	 */
	String CSS_DIALOG_BOX = "configurable-DialogBox";

	/**
	 * Constant for dialog box width.
	 */
	String WIDTH = "100%";

	/**
	 * Constant for panel width.
	 */
	String PANEL_WIDTH = "500px";

	/**
	 * Constant string for empty.
	 */
	String EMPTY = "";

	/**
	 * HORIZONTAL_LINE_CSS {@link String} is a constant for representing the CSS for horizontal line.
	 */
	String HORIZONTAL_LINE_CSS = "horizontalLine";

	/**
	 * WIDTH_30_PX {@link String} is a constant for representing the CSS for width 30 px.
	 */
	String WIDTH_30_PX = "width30px";

	/**
	 * REGEX_BUILDER_SCROLL_CSS {@link String} is a constant for representing the CSS for scroll panel for Regex Builder.
	 */
	String REGEX_BUILDER_SCROLL_CSS = "regexBuilderScrollPanel";

	/**
	 * WIDTH_200_PX {@link String} is a constant for representing the CSS for width 200px.
	 */
	String WIDTH_200_PX = "width200px";

	/**
	 * WIDTH_610_PX {@link String}is a constant for representing the CSS for width 610 px.
	 */
	String WIDTH_610_PX = "width610px";

	/**
	 * TAB_PANEL_REGEX_BUILDER_CSS {@link String} is a constant for representing the CSS for tab panel defined for Regex builder.
	 */
	String TAB_PANEL_REGEX_BUILDER_CSS = "tabPanelRegexBuider";

	/**
	 * MATCHED_AREA_REGEX_BUILDER_CSS {@link String} is a constant for representing the CSS for result area for regex builder.
	 */
	String MATCHED_AREA_REGEX_BUILDER_CSS = "matchedHTMLRegexBuilder";

	/**
	 * _360_PX {@link String} is a constant for representing the CSS for width 360 px.
	 */
	String _360_PX = "360px";

	/**
	 * REGEX_BUILDER_DIALOG_CSS {@link String} is a constant for representing the CSS for Regex Builder dialog.
	 */
	String REGEX_BUILDER_DIALOG_CSS = "regexDialogBox";

	/**
	 * HELP_CONTENT_CSS {@link String}.
	 */
	String HELP_CONTENT_CSS = "helpContent";

	/**
	 * The height of one cell in pixels.
	 */
	int CELL_HEIGHT = 28;

	/**
	 * The default height in terms of row count of a table.
	 */
	int DEFAULT_HEIGHT = 5;

	/**
	 * The default height of header row.
	 */
	int _65PX = 65;

	/**
	 * Constant for ON.
	 */
	String ON = "ON";

	/**
	 * TABLE_HEADER_HEIGHT
	 */
	int TABLE_HEADER_HEIGHT = 40;

	/**
	 * AND {@link String} is a constant used for the "AND" text.
	 */
	String AND = "AND";

	/**
	 * CSS name for adding padding at left and right of element.
	 */
	String PADDING_LEFT_RIGHT_5_CSS = "paddingLeftRight5";

	/**
	 * textAreaRegexBuilderCSS {@link String} CSS for the text area for the Regex Builder.
	 */
	String TEXT_AREA_REGEX_BUILDER_CSS = "textAreaRegexBuilderCSS";

	/**
	 * String constant for select regex button image.
	 */
	String SELECT_REGEX_BUTTON_IMAGE = "selectRegexPatternImage";

	/**
	 * String constant for regex builder image.
	 */
	String SELECT_REGEX_BUILDER_IMAGE = "selectRegexBuilderImage";

	/**
	 * REGEX_BUILDER_RESULT_CSS1 {@link String} a constant used for defining the CSS applied for the result of regex builder.
	 */
	String REGEX_BUILDER_RESULT_CSS1 = "<font class=\"regexBuilderResultFontCSS\"><span class=\"backgroundColorRegexBuilder1\">";

	/**
	 * REGEX_BUILDER_RESULT_CSS2 {@link String} a constant used for defining the CSS applied for the result of regex builder.
	 */
	String REGEX_BUILDER_RESULT_CSS2 = "<font class=\"regexBuilderResultFontCSS\"><span class=\"backgroundColorRegexBuilder2\">";

	/**
	 * The SET_ICON_FOR_SORTING_CSS {@link String} is a CSS for for sort icon on web scanner and upload batch UI.
	 */
	String SET_ICON_FOR_SORTING_CSS = "setIconForSorting";

	/**
	 * WIDTH_144_PX {@link String} is a constant for defining the width 144 px.
	 */
	String WIDTH_144_PX = "width144px";

	/**
	 * The MARGIN_TOP_5_CSS {@link String} is a common CSS for margin top with CSS class named 'marTop5'.
	 */
	String MARGIN_TOP_5_CSS = "marTop5";

	/**
	 * The SET_ICON_FOR_THEME_CSS {@link String} is a CSS for for theme icon.
	 */
	String SET_ICON_FOR_THEME_CSS = "setIconForTheme";

	/**
	 * The SET_CSS_FOR_APPLICATION {@link String} is a CSS for for application tab.
	 */
	String SET_CSS_FOR_APPLICATION = "setCssForApplication";

	/**
	 * DOT {@link String} constant for dot.
	 */
	String DOT = ".";

	/**
	 * NEW_LINE {@link String} constant for new line.
	 */
	String NEW_LINE = "<br>";

	/**
	 * Css for margin on regex pattern selector.
	 */
	String REGEX_PATTERN_SELECTOR_CSS = "setCssForRegexPatternSelector";

	/**
	 * Css for margin on regex pattern selector.
	 */
	String REGEX_GROUP_SELECTOR_CSS = "setCssForRegexGroupSelector";

	/**
	 * Constant used for character '\'.
	 */
	String BACKWARD_SLASH = "\\";

	/**
	 * String constant for blank space.
	 */
	public static final String BLANK = " ";

	/**
	 * Constant for length of maximum description to be displayed on table view.
	 */
	int MAX_DESCRIPTION_LENGTH = 70;

	/**
	 * The _98_PERCENTAGE {@link String} is a common CSS for dimension 98%'.
	 */
	String _98_PERCENTAGE = "98%";

	/**
	 * Constant CSS_ALIGN_RIGHT {@link String} referring CSS class name alignRight for aligning the elements to the right
	 */
	String CSS_ALIGN_RIGHT = "alignRight";

	/**
	 * Constant HTML_TITLE_TAG_NAME used to get DOM element by tag name for setting the value of HTML title for pages.
	 */
	String HTML_TITLE_TAG_NAME = "title";

	/**
	 * The IMAGE_LOGO_TK {@link String} is a CSS for logo images.
	 */
	String IMAGE_LOGO_TK = "imageLogoTK";

	/**
	 * The IMAGE_LOGO {@link String} is a CSS for logo images.
	 */
	String IMAGE_LOGO = "imageLogo";

	/**
	 * Constant for turkish post fix.
	 */
	String TURKISH_POSTFIX = "tk";

	/**
	 * The LOGO_PANEL {@link String} is a CSS for logo panel.
	 */
	String LOGO_PANEL = "logoPanel";

	/**
	 * The HEADER_TABS {@link String} is a CSS for header tabs.
	 */
	String HEADER_TABS = "header_tabs";

	/**
	 * The _10PX {@link String} is a common CSS for dimension 10px'.
	 */
	String _10PX = "10px";

	/**
	 * The _20PX {@link String} is a common CSS for dimension 20px'.
	 */
	String _20PX = "20px";

	/**
	 * The _80_PERCENTAGE {@link String} is a common CSS for dimension 80%'.
	 */
	String _80_PERCENTAGE = "80%";

	/**
	 * TABLE_VIEW_VALID_BUTTON {@link String} is a common CSS for displaying table with valid state.
	 */
	String TABLE_VIEW_VALID_BUTTON = "tableViewValidButton";

	/**
	 * TABLE_VIEW_INVALID_BUTTON {@link String} is a common CSS for displaying table with invalid state.
	 */
	String TABLE_VIEW_INVALID_BUTTON = "tableViewInValidButton";

	/**
	 * The MARGIN_TOP_10_CSS {@link String} is a common CSS for margin top with CSS class named 'marTop10'.
	 */
	String MARGIN_TOP_10_CSS = "marTop10";

	/**
	 * CSS for fixing the table layout attribute.
	 */
	public static final String TABLE_LAYOUT_FIXED = "tableLayoutFixed";

	/**
	 * CSS for edit batch class headers.
	 */
	public static final String BATCH_CLASS_HEADER = "batchClassHeader";

	/**
	 * Constant for Batch Class Management Entry Page name.
	 */
	public static final String BATCH_CLASS_MANAGEMENT_HTML = "BatchClassManagement.html";

	/**
	 * CSS for image magnification icon.
	 */
	public static final String ZOOM_HOVER_CSS = "zoomHoverButton";

	/**
	 * String constant for header for batch class management messages in locale file.
	 */
	public static final String BATCH_CLASS_MANAGEMENT_MESSAGES = "batchClassManagementMessages";

	/**
	 * String constant for header for custom workflow messages in locale file.
	 */
	public static final String CUSTOM_WORKFLOW_MESSAGES = "customWorkflowMessages";

	/**
	 * String constant for header for system config messages in locale file.
	 */
	public static final String SYSTEM_CONFIG_MESSAGES = "systemConfigMessages";

	/**
	 * String constant for header for batch class management constants in locale file.
	 */
	public static final String BATCH_CLASS_MANAGEMENT_CONSTANTS = "batchClassManagementConstants";

	/**
	 * String constant for header for custom workflow constants in locale file.
	 */
	public static final String CUSTOM_WORKFLOW_CONSTANTS = "customWorkflowConstants";

	/**
	 * String constant for header for system config constants in locale file.
	 */
	public static final String SYSTEM_CONFIG_CONSTANTS = "systemConfigConstants";

	/**
	 * String constant for header for theme messages in locale file.
	 */
	public static final String THEME_MESSAGES = "themeMessages";

	/**
	 * String constant for header for theme constants in locale file.
	 */
	public static final String THEME_CONSTANTS = "themeConstants";

	/**
	 * String constant to show error message in case of failure of RPC call.
	 */
	public static final String ERROR_FETCHING_DATA = "errorFetchingData";

	/**
	 * String constant to show error message in case of failure of RPC call.
	 */
	public static final String CSS_INVALID_DOCUMENT_CLASSIFICATION = "invalidDocumentClassification";

	/**
	 * Constant used to add None item in the list box
	 */
	public static final String LISTBOX_NONE_ITEM = "None";

	/**
	 * Maximum OCR Confidence.
	 */
	float MAX_CONFIDENCE = 100;

	/**
	 * The IE_BROWSER {@link String} is a constant for IE browser name.
	 */
	String IE_BROWSER = "msie";

	/**
	 * Constant {@link String} for NBSP character
	 */
	public static final String NBSP_SPACE_CHARACTER = "&nbsp";

	/**
	 * Constant char for Space
	 */
	public static final char SPACE_CHAR = ' ';

	/**
	 * Constant {@link String for Rule name CSS row in Test Table View.
	 */
	public static final String TEST_TABLE_RULE_CSS = "ruleNameBackground";

	/**
	 * String Constant for OFF.
	 */
	String OFF = "OFF";

	/**
	 * The LABEL_CSS {@link String} is a CSS for setting font, size and margin of the label.
	 */
	String LABEL_CSS_DETAIL = "labelCssDetail";
	String DATE_FORMAT = null;

	/**
	 * Used in the label for table column priority.
	 */
	public static final String LABEL_TABLE_COLUMN_PRIORITY = "label_table_column_priority";

	/**
	 * Used in the label for table column batchId.
	 */
	public static final String LABEL_TABLE_COLUMN_BATCHID = "label_table_column_batchId";

	/**
	 * Used in the label for table column batch class name.
	 */
	public static final String LABEL_TABLE_COLUMN_BATCHCLASSNAME = "label_table_column_batchClassName";

	/**
	 * Used in the label for table column batch update date.
	 */
	public static final String LABEL_TABLE_COLUMN_BATCHUPDATEDON = "label_table_column_batchUpdatedOn";

	/**
	 * Used in the label for table column batch imported date.
	 */
	public static final String LABEL_TABLE_COLUMN_BATCHIMPORTEDON = "label_table_column_batchImportedOn";

	/**
	 * Used in the label for table column batch status.
	 */
	public static final String LABEL_TABLE_COLUMN_BATCHSTATUS = "label_table_column_batchStatus";

	/**
	 * Used in the label for table column batch name.
	 */
	public static final String LABEL_TABLE_COLUMN_BATCHNAME = "label_table_column_batchName";

	/**
	 * 
	 */
	public static final String FOLDER_NAME_VALIDATION_PATTERN = "(.*)[:?*\\\\<>/|\"](.*)";

	/**
	 * Used in the label for table column select.
	 */
	public static final String LABEL_TABLE_COLUMN_SELECT = "label_table_column_select";

	/**
	 * Used in the label for table column user name.
	 */
	public static final String LABEL_TABLE_COLUMN_USERNAME = "label_table_column_username";

	/**
	 * Used in the label for table column password.
	 */
	public static final String LABEL_TABLE_COLUMN_PASSWORD = "label_table_column_password";

	/**
	 * Used in the label for table column server name.
	 */
	public static final String LABEL_TABLE_COLUMN_INCOMING_SERVER = "label_table_column_incoming_server";

	/**
	 * Used in the label for table column server type.
	 */
	public static final String LABEL_TABLE_COLUMN_SERVERTYPE = "label_table_column_servertype";

	/**
	 * Used in the label for table column inbox.
	 */
	public static final String LABEL_TABLE_COLUMN_FOLDER = "label_table_column_folder";

	/**
	 * Used in the label for table column ssl.
	 */
	public static final String LABEL_TABLE_COLUMN_SSL = "label_table_column_ssl";

	/**
	 * Used in the label for table column enable.
	 */
	public static final String LABEL_TABLE_COLUMN_ENABLE = "label_table_column_enable";

	/**
	 * Used in the label for table column port.
	 */
	public static final String LABEL_TABLE_COLUMN_PORT = "label_table_column_port";

	/**
	 * Used in the label for table column server url.
	 */
	public static final String LABEL_TABLE_COLUMN_SERVERURL = "label_table_column_serverurl";

	/**
	 * Used in the label for table column repository id.
	 */
	public static final String LABEL_TABLE_COLUMN_REPOSITORYID = "label_table_column_repositoryid";

	/**
	 * Used in the label for table column file extension.
	 */
	public static final String LABEL_TABLE_COLUMN_FILE_EXTENSION = "label_table_column_file_extension";

	/**
	 * Used in the label for table column property.
	 */
	public static final String LABEL_TABLE_COLUMN_PROPERTY = "label_table_column_property";

	/**
	 * Used in the label for table column value.
	 */
	public static final String LABEL_TABLE_COLUMN_VALUE = "label_table_column_value";

	/**
	 * Used in the label for table column new value.
	 */
	public static final String LABEL_TABLE_COLUMN_NEW_VALUE = "label_table_column_new_value";

	/**
	 * Used in the label for table column profile name.
	 */
	public static final String LABEL_COLUMN_PROFILE_NAME = "label_column_profile_name";

	/**
	 * Used in the label for table column current pixel type.
	 */
	public static final String LABEL_COLUMN_CURRENT_PIXEL_TYPE = "label_column_current_pixel_type";

	/**
	 * Used in the label for table column bit depth.
	 */
	public static final String LABEL_COLUMN_BIT_DEPTH = "label_column_bit_depth";

	/**
	 * Used in the label for table column multi transfer.
	 */
	public static final String LABEL_COLUMN_MULTI_TRANSFER = "label_column_multi_transfer";

	/**
	 * Used in the label for table column hide ui.
	 */
	public static final String LABEL_COLUMN_HIDE_UI = "label_column_hide_ui";

	/**
	 * Used in the label for table column select feeder.
	 */
	public static final String LABEL_COLUMN_SELECT_FEEDER = "label_column_select_feeder";

	/**
	 * Used in the label for table column auto scan.
	 */
	public static final String LABEL_COLUMN_AUTO_SCAN = "label_column_auto_scan";

	/**
	 * Used in the label for table column enable duplex.
	 */
	public static final String LABEL_COLUMN_ENABLE_DUPLEX = "label_column_enable_duplex";

	/**
	 * Used in the label for table column blank page mode.
	 */
	public static final String LABEL_COLUMN_BALNK_PAGE_MODE = "label_column_blank_page_mode";

	/**
	 * Used in the label for table column blank page threshold.
	 */
	public static final String LABEL_COLUMN_BALNK_PAGE_THRESHOLD = "label_column_blank_page_threshold";

	/**
	 * Used in the label for table column dpi.
	 */
	public static final String LABEL_COLUMN_DPI = "label_column_dpi";

	/**
	 * Used in the label for table column color.
	 */
	public static final String LABEL_COLUMN_COLOR = "label_column_color";

	/**
	 * Used in the label for table column paper size.
	 */
	public static final String LABEL_COLUMN_PAPER_SIZE = "label_column_paper_size";

	/**
	 * Used in the label for table column back page rotation multiple.
	 */
	public static final String LABEL_COLUMN_BACK_PAGE_ROTATION = "label_column_back_page_rotation";

	/**
	 * Used in the label for table column page cache clear count.
	 */
	public static final String LABEL_COLUMN_PAGE_CACHE_COUNT = "label_column_page_cache_count";

	/**
	 * Used in the label for table column back front rotation multiple.
	 */
	public static final String LABEL_COLUMN_FRONT_PAGE_ROTATION = "label_column_front_page_rotation";

	/**
	 * Used in the label for table column enable multifeed error detection.
	 */
	public static final String LABEL_COLUMN_ERROR_DETECTION = "label_column_error_detection";

	/**
	 * Used in the label for table column enable scan.
	 */
	public static final String LABEL_COLUMN_ENABLE_RESCAN = "label_column_enable_rescan";

	/**
	 * Used in the label for table column enable scan.
	 */
	public static final String LABEL_COLUMN_ENABLE_DELETE = "label_column_enable_delete";

	/**
	 * Used in the label for table column name.
	 */
	public static final String LABEL_COLUMN_NAME = "label_column_name";

	/**
	 * Used in the label for table column description.
	 */
	public static final String LABEL_COLUMN_DESCRIPTION = "label_column_description";

	/**
	 * Used in the label for table column type.
	 */
	public static final String LABEL_COLUMN_TYPE = "label_column_type";

	/**
	 * Used in the label for table column field order.
	 */
	public static final String LABEL_COLUMN_FIELD_ORDER = "label_column_field_order";

	/**
	 * Used in the label for table column sample value.
	 */
	public static final String LABEL_COLUMN_SAMPLE_VALUE = "label_column_sample_value";

	/**
	 * Used in the label for table column validation pattern.
	 */
	public static final String LABEL_COLUMN_VALIDATION_PATTERN = "label_column_validation_pattern";

	/**
	 * Used in the label for table column validation pattern.
	 */
	public static final String LABEL_COLUMN_FIELD_OPTION_VALUE_LIST = "label_column_field_option_value_list";

	public static final String STAR = "*";

	/**
	 * OR {@link String} is a constant used for the "OR" text.
	 */
	public static final String OR = "OR";

	/**
	 * SELECT_LABEL {@link String} is a constant used for the "Select" text.
	 */
	public static final String SELECT_LABEL = "select";

	/**
	 * Constant for character '/'.
	 */
	public static final String FORWARD_SLASH = "/";

	/**
	 * Constant for defining the name of error page for invalid license.
	 */
	public static final String LICENSE_ERROR_PAGE_PATH = "InvalidLicense.html";

	/**
	 * Constant for link for "ephesoft.com".
	 */
	public static final String EPHESOFT_LINK = EphesoftUIContext.getFooterLink();

	public static final String MENU_ITEM_HEIGHT_CSS = "menuItem_height";

	public static final String TEXTBOX_HEIGHT_CSS = "textBox_height";

	public static final String TOP_PANEL_WIDGET_CSS = "topPanelWidget";

	public static final String _150PX = "150px";

	public static final String _195PX = "195px";

	public static final String GWT_MENU_ITEM_SELECTED_CSS = "gwt-MenuItem-selected";

	public static final String _50_PERCENTAGE = "50%";

	public static final String ZOOM_IN_BUTTON_CSS = "zoomInButton";

	public static final String ZOOM_OUT_BUTTON_CSS = "zoomOutButton";

	public static final String ROTATE_BUTTON_CSS = "rotateButton";

	public static final String FIT_TO_SIZE_BUTTON_CSS = "fitToPageButton";

	public static final String FIT_TO_PAGE_IMAGE_CSS = "fitToPageImage";

	public static final String BUTTON_LAST_CSS = "btnlast";

	public static final String BUTTON_DISABLED_CSS = "btndisabled";

	public static final String TOP_PANEL = "top_Panel";

	public static final String MENU_BAR = "menuBar";

	public static final String BUTTON_PANEL = "buttonPanel";

	public static final String BUTTON_BOTTOM_PANEL = "buttonBottomPanel";

	public static final String DRAGGABLE_IMAGE_CSS = "draggableImage";

	public static final String THUMBSTACK_PANEL_CSS = "thumbStackPanel";

	public static final String BORDER_IMAGE_THUMB_CSS = "borderImageThumb";

	public static final String MAIN_PAGE = "mainPage";

	public static final String LEFT_PANEL = "leftPanel";

	public static final String LEFT_BOTTOM_PANEL = "leftBottomPanel";

	public static final String MIDDLE_PANEL = "middlePanel";

	public static final String RIGHT_PANEL = "rightPanel";

	public static final String POWERED_BY_EPHESOFT_LABEL = "poweredByEphesoftLabel";

	public static final String _30_PERCENTAGE = "30%";

	public static final String ERROR_STYLE_CSS = "errorStyle";

	public static final String _200PX = "200px";

	public static final String GRID_VIEW_MAIN_PANEL_CSS = "gridViewMainPanel";

	public static final String GRID_VIEW_CSS = "gridView";

	public static final String GRID_PANEL = "gridPanel";

	public static final String BOTTOM_DETAIL_PANEL = "bottomDetailPanel";

	public static final String LEFT_PANEL_CONTENT_PANEL = "leftPanelContentPanel";

	public static final String MAIN_PANEL = "mainPanel";

	public static final String SUB_MAIN_PANEL = "subMainPanel";

	public static final String BOTTOM_PANEL = "bottomPanel";

	public static final String PANEL_HEADER = "panelHeader";

	public static final String UPLOAD_BATCH_GRID_FILE_NAME = "upload_batch_grid_file_name";
	public static final String UPLOAD_BATCH_GRID_PROGRESS = "upload_batch_grid_progress";
	public static final String UPLOAD_BATCH_GRID_FILE_TYPE = "upload_batch_grid_file_type";
	public static final String UPLOAD_BATCH_GRID_FILE_SIZE = "upload_batch_grid_file_size";

	public static final String MENU_ITEM_DISABLE_CSS = "menuItemDisable";

	public static final String PLEASE_WAIT = "please_wait";

	public static final String LABEL_COLUMN_METHOD_NAME = "label_column_method_name";

	public static final String LABEL_COLUMN_METHOD_DESCRIPTION = "label_column_method_description";

	public static final String LABEL_COLUMN_SHORTCUT_KEY = "label_column_shortcut_key";

	public static final String LABEL_COLUMN_VALIDATION_RULE_OPERATOR = "label_column_validation_rule_operator";

	public static final String LABEL_COLUMN_REMOVE_INVALID_ROWS = "label_column_remove_invalid_rows";

	public static final String LABEL_COLUMN_CURRENCY = "label_column_currency";

	public static final String LABEL_COLUMN_COLUMN_NAME = "label_column_column_name";

	public static final String LABEL_COLUMN_ALTERNATE_VALUES = "label_column_alternate_values";

	public static final String LABEL_COLUMN_RULE_NAME = "label_column_rule_name";

	public static final String LABEL_COLUMN_START_PATTERN = "label_column_start_pattern";

	public static final String LABEL_COLUMN_END_PATTERN = "label_column_end_pattern";

	public static final String LABEL_COLUMN_TABLE_EXTRACTION_API = "label_column_table_extraction_api";

	public static final String LABEL_COLUMN_COLUMN_PATTERN = "label_column_column_pattern";

	public static final String LABEL_COLUMN_BETWEEN_LEFT = "label_column_between_left";

	public static final String LABEL_COLUMN_BETWEEN_RIGHT = "label_column_between_right";

	public static final String LABEL_COLUMN_COLUMN_HEADER_PATTERN = "label_column_column_header_pattern";

	public static final String LABEL_COLUMN_START_COORDINATE = "label_column_start_coordinate";

	public static final String LABEL_COLUMN_END_COORDINATE = "label_column_end_coordinate";

	public static final String LABEL_COLUMN_MULTILINE_ANCHOR = "label_column_multiline_anchor";

	public static final String LABEL_COLUMN_REQUIRED = "label_column_required";

	public static final String LABEL_COLUMN_EXTRACTED_DATA_FROM_COLUMN = "label_column_extracted_data_from_column";

	public static final String LABEL_COLUMN_RULE = "label_column_rule";

	public static final String LABEL_COLUMN_SET_RULE = "label_column_set_rule";

	public static final String HEADER_PROFILE_NAME = "Profile Name";

	public static final String HEADER_CURRENT_PIXEL_TYPE = "Current Pixel Type";

	public static final String HEADER_BIT_DEPTH = "Bit Depth";

	public static final String HEADER_MULTI_TRANSFER = "Multi Transfer";

	public static final String HEADER_HIDE_UI = "Hide UI";

	public static final String HEADER_SELECT_FEEDER = "Select Feeder";

	public static final String HEADER_AUTO_SCAN = "Auto Scan";

	public static final String HEADER_ENABLE_DUPLEX = "Enable Duplex";

	public static final String HEADER_BLANK_PAGE_MODE = "Blank Page Mode";

	public static final String HEADER_BLANK_PAGE_THRESHOLD = "Blank Page Threshold";

	public static final String HEADER_DPI = "DPI";

	public static final String HEADER_COLOR = "Color";

	public static final String HEADER_PAPER_SIZE = "Paper Size";

	public static final String HEADER_BACK_PAGE_ROTATION_MULTIPLE = "Back Page Rotation Multiple";

	public static final String HEADER_PAGES_CACHE_CLEAR_COUNT = "Pages Cache Clear Count";

	public static final String HEADER_FRONT_PAGE_ROTATION_MULTIPLE = "Front Page Rotation Multiple";

	public static final String HEADER_ENABLE_MULTIFEED_ERROR_DETECTION = "Enable Multifeed Error Detection";

	public static final String HEADER_ENABLE_RESCAN = "Enable Rescan";

	public static final String HEADER_ENABLE_DELETE = "Enable Delete";

	public static final String NO_RECORDS_FOUND = "no_records_found";

	public static final String LABEL_COLUMN_COORDINATES = "label_column_coordinates";

	public static final String LABEL_COLUMN_HEADER = "label_column_header";

	public static final String LABEL_REGEX_EXTRACTION = "label_regex_extraction";

	public static final String VALIDATE_GRID_ERR_MSG = "validate_grid_err_msg";
	public static final String FM_NAME_COLUMN_HEADER = "fm_name_column_header";

	public static final String FM_SELECT_COLUMN_HEADER = "fm_select_column_header";

	public static final String FM_ICON_COLUMN_HEADER = "fm_icon_column_header";

	public static final String FM_MODIFIED_COLUMN_HEADER = "fm_modified_column_header";

	public static final String FM_SIZE_COLUMN_HEADER = "fm_size_column_header";

	public static final String FM_FILE_TYPE_COLUMN_HEADER = "fm_file_type_column_header";

	public static final String FM_PROGRESS_COLUMN_HEADER = "fm_progress_column_header";

	public static final String LICENSE_GRID_PROPERTY_NAME_HEADER = "license_grid_property_name_header";

	public static final String LICENSE_GRID_PROPERTY_VALUE_HEADER = "license_grid_property_value_header";

	public static final String PLUGIN_GRID_DEPENDENCY_HEADER = "plugin_grid_dependency_header";

	public static final String PLUGIN_GRID_DEPENDENCY_TYPE_HEADER = "plugin_grid_dependency_type_header";

	public static final String CM_CONNECTION_NAME_HEADER = "cm_connection_name_header";

	public static final String CM_CONNECTION_DESCRIPTION_HEADER = "cm_connection_name_description_header";

	public static final String REGEX_PATTERN_COLUMN_HEADER = "regex_pattern_column_header";

	public static final String REGEX_PATTERN_DESCRIPTION_COLUMN_HEADER = "regex_pattern_description_column_header";

	public static final String REGEX_GROUP_NAME_HEADER = "regex_group_name_header";

	public static final String REGEX_VALIDATION_GRID_PATTERN = "regex_validation_grid_pattern";

	public static final String REGEX_PATTERN_BUTTON_GRID = "regex_pattern_button_grid";

	public static final String REGEX_PATTERN_BUTTON_GRID_DECRIPTION_HEADER = "regex_pattern_button_grid_decription_header";

	public static final String REGEX_GROUP_BUTTON_GRID = "regex_group_button_grid";

	public static final String COLUMN_CONFIG_NAME = "column_config_name";

	public static final String COLUMN_CONFIG_IDENTIFIER = "column_config_identifier";

	public static final String COLUMN_CONFIG_DESCRIPTION = "column_config_description";

	public static final String COLUMN_CONFIG_PATTERN = "column_config_pattern";

	public static final String COLUMN_CONFIG_PRIORITY = "column_config_priority";

	public static final String COLUMN_CONFIG_HIDDEN = "column_config_hidden";

	public static final String COLUMN_CONFIG_FIELD_NAME = "column_config_field_name";

	public static final String COLUMN_CONFIG_KEY = "column_config_key";

	public static final String COLUMN_CONFIG_VALUE = "column_config_value";

	public static final String BCC_UNC_PATH = "bcc_unc_path";

	public static final String BCC_VERSION = "bcc_version";

	public static final String BCC_ENCRYPTION = "bcc_encryption";

	public static final String BCC_CURRENT_USER = "bcc_current_user";

	public static final String BCC_ROLES = "bcc_roles";

	public static final String DT_MINIMUM_CONFIDENCE_THRESHOLD = "dt_minimum_confidence_threshold";

	public static final String DT_FIRST_PAGE = "dt_first_page";

	public static final String DT_SECOND_PAGE = "dt_second_page";

	public static final String DT_THIRD_PAGE = "dt_third_page";

	public static final String DT_LASTGREATERTHAN3_PAGE = "dt_lastgreaterthan3_page";

	public static final String INDEX_FIELD_DATA_TYPE = "index_field_data_type";

	public static final String INDEX_FIELD_OCR_THRESHOLD = "index_field_ocr_threshold";

	public static final String INDEX_FIELD_TYPE = "index_field_type";

	public static final String INDEX_FIELD_CATEGORY = "index_field_category";

	public static final String INDEX_FIELD_ORDER = "index_field_order";

	public static final String INDEX_FIELD_SAMPLE_VALUE = "index_field_sample_value";

	public static final String INDEX_FIELD_OPTION_VALUES_LIST = "index_field_option_values_list";

	public static final String INDEX_FIELD_BARCODE = "index_field_barcode";

	public static final String DB_EXPORT_DB_TABLE_NAME = "db_export_db_table_name";

	public static final String DB_EXPORT_DB_COLUMN_NAME = "db_export_db_column_name";

	public static final String KV_NO_OF_WORDS = "kv_no_of_words";

	public static final String KV_FUZINESS = "kv_fuziness";

	public static final String KV_LOCATION = "kv_location";

	public static final String KV_WEIGHT = "kv_weight";

	public static final String FUZZY_DB_TABLE_COLUMN_NAME = "fuzzy_db_table_column_name";

	public static final String FUZZY_DB_IS_SEARCHABLE = "fuzzy_db_is_searchable";

	public static final String ADVANCE_KV_CONFIDENCE = "advance_kv_confidence";

	public static final String ADVANCE_KV_KEY_COORDINATES = "advance_kv_key_coordinates";

	public static final String ADVANCE_KV_VALUE_COORDINATES = "advance_kv_value_coordinates";

	public static final String ADVANCE_KV_COLORCODE = "advance_kv_colorcode";

	public static final String POWERED_BY_EPHESOFT_LINK_LABEL = "Powered By Ephesoft";

	public static final String BIM_COLUMN_CONFIG_BATCH_NAME = "bim_column_config_batch_name";

	public static final String BIM_COLUMN_CONFIG_STATUS = "bim_column_config_status";

	public static final String BIM_COLUMN_CONFIG_PRIORITY = "bim_column_config_priority";

	public static final String BIM_COLUMN_CONFIG_BATCH_CLASS_NAME = "bim_column_config_batch_class_name";

	public static final String BIM_COLUMN_CONFIG_CURRENT_USER = "bim_column_config_current_user";

	public static final String BIM_COLUMN_CONFIG_IMPORTED_ON = "bim_column_config_imported_on";

	public static final String BIM_COLUMN_CONFIG_LAST_MODIFIED = "bim_column_config_last_modified";

	public static final String CLASSIFICATION_TYPE = "classification_type";

	public static final String DOCUMENT_TYPE = "document_type";

	public static final String DOCUMENT_ID = "document_id";

	public static final String DOCUMENT_CONFIDENCE = "document_confidence";

	public static final String PAGE_NAME = "page_name";

	public static final String PAGE_ID = "page_id";

	public static final String PAGE_CLASSIFICATION_VALUE = "page_classification_value";

	public static final String PAGE_CONFIDENCE = "page_confidence";

	public static final String CLASSIFICATION_SAMPLE = "classification_sample";

	public static final String FILE_NAME = "file_name";

	public static final String PAGE_NUMBER = "page_number";

	public static final String PAGE_TYPE = "page_type";

	public static final String IMAGE_CLASSIFICATION = "image_classification";

	public static final String LUCENE_LEARNING = "lucene_learning";

	public static final String YES = "yes";

	public static final String OK = "ok";

	public static final String NO = "no";

	public static final String CLOSE = "close";

	public static final String CANCEL = "cancel";

	public static final int DOC_LIMIT = 100;
}
