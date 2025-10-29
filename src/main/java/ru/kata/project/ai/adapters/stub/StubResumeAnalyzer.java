package ru.kata.project.ai.adapters.stub;

import ru.kata.project.ai.core.entity.AnalysisContext;
import ru.kata.project.ai.core.entity.AnalysisResult;
import ru.kata.project.ai.core.entity.Recommendation;
import ru.kata.project.ai.core.entity.ResumeId;
import ru.kata.project.ai.core.entity.Section;
import ru.kata.project.ai.core.entity.Severity;
import ru.kata.project.ai.core.port.ResumeAnalyzer;

import java.time.Instant;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * StubResumeAnalyzer
 * <p>
 * Класс-заглушка, представляющий анализатор резюме для пользователя {@link ResumeAnalyzer}.
 * </p>
 * <p>
 * Возвращает 3 рекомендации по улучшению резюме, переведённые по локали, предоставленной пользователем.
 * </p>
 *
 * @author Vladislav_Bogomolov
 */
public class StubResumeAnalyzer implements ResumeAnalyzer {
    private static final String TEMPLATE_VERSION = "stub-1.0.0";

    @Override
    public AnalysisResult analyze(ResumeId id, AnalysisContext context) {
        return new AnalysisResult(id,
                List.of(
                        new Recommendation("SKILLS_TOO_BROAD",
                                Severity.LOW,
                                localize("Skills are too general, prefer concrete technologies", context.locale()),
                                Section.SKILLS,
                                Map.of("example", "Java, Spring")),
                        new Recommendation(
                                "SUMMARY_MISSING",
                                Severity.MEDIUM,
                                localize("Summary is short or missing", context.locale()),
                                Section.SUMMARY,
                                Map.of("expectedLength", 120)),
                        new Recommendation("EXPERIENCE_LACKING",
                                Severity.HIGH,
                                localize("Consider expanding experience descriptions with metrics", context.locale()),
                                Section.EXPERIENCE,
                                null)
                ),
                Instant.now(), TEMPLATE_VERSION);
    }

    private String localize(String msg, Locale locale) {
        if ("ru".equals(locale.getLanguage())) {
            if (msg.contains("Skills are too general, prefer concrete technologies")) {
                return "Навыки слишком общие, укажите конкретные технологии";
            }
            if (msg.contains("Summary is short or missing")) {
                return "Краткое описание отсутствует или слишком короткое";
            }
            if (msg.contains("Consider expanding experience descriptions with metrics")) {
                return "Рекомендуется расширить описание опыта с метриками";
            }
        }
        return msg;
    }
}