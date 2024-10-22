package com.ironsoftware.ironpdf.render;

/**
 * Defines the target virtual paper size the PDF.  Relates to real-world paper-sizes.
 */
public enum PaperSize {
    /**
     * 8.5in X 11in
     */
    LETTER,

    /**
     * 8.5in X 14in
     */
    LEGAL,

    /**
     * 210mm X 297mm
     */
    A4,

    /**
     * 17in X 22in
     */
    C_SHEET,

    /**
     * 22in X 34in
     */
    D_SHEET,

    /**
     * 34in X 44in
     */
    E_SHEET,

    /**
     * 8.5in X 11in
     */
    LETTER_SMALL,

    /**
     * 11in X 17in
     */
    TABLOID,

    /**
     * 17in X 11in
     */
    LEDGER,

    /**
     * 5.5in X 8.5in
     */
    STATEMENT,

    /**
     * 7.25in X 10.5in
     */
    EXECUTIVE,

    /**
     * 297mm X 420mm
     */
    A3,

    /**
     * 210mm X 297mm
     */
    A4_SMALL,

    /**
     * 148mm X 210mm
     */
    A5,

    /**
     * 250mm X 353mm
     */
    B4,

    /**
     * 176mm X 250mm
     */
    B5,

    /**
     * 8.5in X 13in
     */
    FOLIO,

    /**
     * 215mm X 275mm
     */
    QUARTO,

    /**
     * 10in X 14in
     */
    STANDARD_10X14,

    /**
     * 11in X 17in
     */
    STANDARD_11X17,

    /**
     * 8.5in X 11in
     */
    NOTE,

    /**
     * 3.875in X 8.875in
     */
    NUMBER_9_ENVELOPE,

    /**
     * 4.125in X 9.5in
     */
    NUMBER_10_ENVELOPE,

    /**
     * 4.5in X 10.375in
     */
    NUMBER_11_ENVELOPE,

    /**
     * 4.75in X 11in
     */
    NUMBER_12_ENVELOPE,

    /**
     * 5in X 11.5in
     */
    NUMBER_14_ENVELOPE,

    /**
     * 110mm X 220mm
     */
    DL_ENVELOPE,

    /**
     * 162mm X 229mm
     */
    C5_ENVELOPE,

    /**
     * 324mm X 458mm
     */
    C3_ENVELOPE,

    /**
     * 229mm X 324mm
     */
    C4_ENVELOPE,

    /**
     * 114mm X 162mm
     */
    C6_ENVELOPE,

    /**
     * 114mm X 229mm
     */
    C65_ENVELOPE,

    /**
     * 250mm X 353mm
     */
    B4_ENVELOPE,

    /**
     * 176mm X 250mm
     */
    B5_ENVELOPE,

    /**
     * 176mm X 125mm
     */
    B6_ENVELOPE,

    /**
     * 110mm X 230mm
     */
    ITALY_ENVELOPE,

    /**
     * 3.875in X 7.5in
     */
    MONARCH_ENVELOPE,

    /**
     * 3.625in X 6.5in
     */
    PERSONAL_ENVELOPE,

    /**
     * 14.875in X 11in
     */
    US_STANDARD_FAN_FOLD,

    /**
     * 8.5in X 12in
     */
    GERMAN_STANDARD_FAN_FOLD,

    /**
     * 8.5in X 13in
     */
    GERMAN_LEGAL_FAN_FOLD,

    /**
     * 250mm X 353mm
     */
    ISO_B4,

    /**
     * 100mm X 148mm
     */
    JAPANESE_POSTCARD,

    /**
     * 9in X 11in
     */
    STANDARD_9X11,

    /**
     * 10in X 11in
     */
    STANDARD_10X11,

    /**
     * 15in X 11in
     */
    STANDARD_15X11,

    /**
     * 220mm X 220mm
     */
    INVITE_ENVELOPE,

    /**
     * 9.275in X 12in
     */
    LETTER_EXTRA,

    /**
     * 9.275in X 15in
     */
    LEGAL_EXTRA,

    /**
     * 11.69in X 18in
     */
    TABLOID_EXTRA,

    /**
     * 236mm X 322mm
     */
    A4_EXTRA,

    /**
     * 8.275in X 11in
     */
    LETTER_TRANSVERSE,

    /**
     * 210mm X 297mm
     */
    A4_TRANSVERSE,

    /**
     * 9.275in X 12in
     */
    LETTER_EXTRA_TRANSVERSE,

    /**
     * 227mm X 356mm
     */
    A_PLUS,

    /**
     * 305mm X 487mm
     */
    B_PLUS,

    /**
     * 8.5in X 12.69in
     */
    LETTER_PLUS,

    /**
     * 210mm X 330mm
     */
    A4_PLUS,

    /**
     * 148mm X 210mm
     */
    A5_TRANSVERSE,

    /**
     * 182mm X 257mm
     */
    B5_TRANSVERSE,

    /**
     * 322mm X 445mm
     */
    A3_EXTRA,

    /**
     * 174mm X 235mm
     */
    A5_EXTRA,

    /**
     * 201mm X 276mm
     */
    B5_EXTRA,

    /**
     * 420mm X 594mm
     */
    A2,

    /**
     * 297mm X 420mm
     */
    A3_TRANSVERSE,

    /**
     * 322mm X 445mm
     */
    A3_EXTRA_TRANSVERSE,

    /**
     * 200mm X 148mm
     */
    JAPANESE_DOUBLE_POSTCARD,

    /**
     * 105mm X 148mm
     */
    A6,

    /**
     * 11in X 8.5in
     */
    LETTER_ROTATED,

    /**
     * 420mm X 297mm
     */
    A3_ROTATED,

    /**
     * 297mm X 210mm
     */
    A4_ROTATED,

    /**
     * 210mm X 148mm
     */
    A5_ROTATED,

    /**
     * 364mm X 257mm
     */
    B4_JIS_ROTATED,

    /**
     * 257mm X 182mm
     */
    B5_JIS_ROTATED,

    /**
     * 148mm X 100mm
     */
    JAPANESE_POST_CARD_ROTATED,

    /**
     * 148mm X 200mm
     */
    JAPANESE_DOUBLE_POSTCARD_ROTATED,

    /**
     * 148mm X 105mm
     */
    A6_ROTATED,

    /**
     * 128mm X 182mm
     */
    B6_JIS,

    /**
     * 182mm X 128mm
     */
    B6_JIS_ROTATED,

    /**
     * 12in X 11in
     */
    STANDARD_12X11,

    /**
     * 146mm X 215mm
     */
    PRC_16K,

    /**
     * 97mm X 151mm
     */
    PRC_32K,

    /**
     * 97mm X 151mm
     */
    PRC_32K_BIG,

    /**
     * 102mm X 165mm
     */
    PRC_ENVELOPE_NUMBER1,

    /**
     * 102mm X 176mm
     */
    PRC_ENVELOPE_NUMBER2,

    /**
     * 125mm X 176mm
     */
    PRC_ENVELOP_NUMBER3,

    /**
     * 110mm X 208mm
     */
    PRC_ENVELOPE_NUMBER4,

    /**
     * 110mm X 220mm
     */
    PRC_ENVELOPE_NUMBER5,

    /**
     * 120mm X 230mm
     */
    PRC_ENVELOPE_NUMBER6,

    /**
     * 160mm X 230mm
     */
    PRC_ENVELOPE_NUMBER7,

    /**
     * 120mm X 309mm
     */
    PRC_ENVELOPE_NUMBER8,

    /**
     * 229mm X 324mm
     */
    PRC_ENVELOPE_NUMBER9,

    /**
     * 324mm X 458mm
     */
    PRC_ENVELOPE_NUMBER10,

    /**
     * 146mm X 215mm
     */
    PRC16K_ROTATED,

    /**
     * 97mm X 151mm
     */
    PRC_32K_ROTATED,

    /**
     * 97mm X 151mm
     */
    PRC_32K_BIG_ROTATED,

    /**
     * 165mm X 102mm
     */
    PRC_ENVELOPE_NUMBER1_ROTATED,

    /**
     * 176mm X 102mm
     */
    PRC_ENVELOPE_NUMBER2_ROTATED,

    /**
     * 176mm X 125mm
     */
    PRC_ENVELOPE_NUMBER3_ROTATED,

    /**
     * 208mm X 110mm
     */
    PRC_ENVELOPE_NUMBER4_ROTATED,

    /**
     * 220mm X 110mm
     */
    PRC_ENVELOPE_NUMBER5_ROTATED,

    /**
     * 230mm X 120mm
     */
    PRC_ENVELOPE_NUMBER6_ROTATED,

    /**
     * 230mm X 160mm
     */
    PRC_ENVELOPE_NUMBER7_ROTATED,

    /**
     * 309mm X 120mm
     */
    PRC_ENVELOPE_NUMBER8_ROTATED,

    /**
     * 324mm X 229mm
     */
    PRC_ENVELOPE_NUMBER9_ROTATED,

    /**
     * 458mm X 324mm
     */
    PRC_ENVELOPE_NUMBER10_ROTATED,

    /**
     * Set using SetCustomPaperSizeInInches or SetCustomPaperSizeInMillimeters
     */
    Custom
}
