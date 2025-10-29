package ru.kata.project.ai.core.entity;

/**
 * Severity
 * <p>
 * Уровень важности рекомендации при анализе резюме.
 * </p>
 * <p>
 * Используется в объекте {@link Recommendation}
 * для указания приоритета улучшения, найденного анализатором.
 * </p>
 * <p>
 * Описание уровней:
 *  <ul>
 *      <li>{@link #LOW} — несущественная рекомендация;</li>
 *      <li>{@link #MEDIUM} — важная, но не критическая рекомендация;</li>
 *      <li>{@link #HIGH} — критическая рекомендация, требующая обязательного исправления.</li>
 *  </ul>
 * </p>
 *
 * @author Vladislav_Bogomolov
 */
public enum Severity {
    LOW,
    MEDIUM,
    HIGH
}
