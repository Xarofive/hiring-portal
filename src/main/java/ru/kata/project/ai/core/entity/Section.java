package ru.kata.project.ai.core.entity;

/**
 * Section
 * <p>
 * Раздел резюме, к которому относится рекомендация или анализируемая информация.
 * <p>
 * Используется в {@link Recommendation}
 * для указания контекста, где найдено улучшение или проблема.
 * </p>
 * <p>
 * Описание секций:
 *  <ul>
 *      <li>{@link #GENERAL} — общие сведения о резюме;</li>
 *      <li>{@link #SUMMARY} — краткое описание профиля;</li>
 *      <li>{@link #SKILLS} — раздел с перечислением ключевых навыков;</li>
 *      <li>{@link #EXPERIENCE} — опыт работы и достижения;</li>
 *      <li>{@link #EDUCATION} — сведения об образовании;</li>
 *      <li>{@link #PROJECTS} — личные или профессиональные проекты;</li>
 *      <li>{@link #LANGUAGES} — языки программирования и владения языками общения;</li>
 *      <li>{@link #LINKS} — ссылки на портфолио, GitHub, LinkedIn и т.п.;</li>
 *      <li>{@link #CONTACTS} — контактная информация кандидата.</li>
 *  </ul>
 * </p>
 *
 * @author Vladislav_Bogomolov
 */
public enum Section {
    GENERAL,
    SUMMARY,
    SKILLS,
    EXPERIENCE,
    EDUCATION,
    PROJECTS,
    LANGUAGES,
    LINKS,
    CONTACTS
}
