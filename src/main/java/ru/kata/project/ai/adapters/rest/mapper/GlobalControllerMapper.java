package ru.kata.project.ai.adapters.rest.mapper;

import ru.kata.project.ai.adapters.rest.dto.AnalysisResultDto;
import ru.kata.project.ai.adapters.rest.dto.AnalyzeRequestDto;
import ru.kata.project.ai.adapters.rest.dto.EducationDto;
import ru.kata.project.ai.adapters.rest.dto.ExperienceDto;
import ru.kata.project.ai.adapters.rest.dto.GenerateRequestDto;
import ru.kata.project.ai.adapters.rest.dto.GeneratedResumeDto;
import ru.kata.project.ai.core.entity.AnalysisContext;
import ru.kata.project.ai.core.entity.AnalysisResult;
import ru.kata.project.ai.core.entity.EducationItem;
import ru.kata.project.ai.core.entity.ExperienceItem;
import ru.kata.project.ai.core.entity.GeneratedResume;
import ru.kata.project.ai.core.entity.GenerationContext;

import java.time.Year;
import java.time.YearMonth;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

public final class GlobalControllerMapper {

    private static final Locale DEFAULT_LOCALE = Locale.ENGLISH;

    private GlobalControllerMapper() {
    }

    public static AnalysisContext toDomain(AnalyzeRequestDto dto) {
        if (dto == null) {
            return null;
        }
        return new AnalysisContext(parseLocale(dto.locale()), dto.targetRole(), dto.targetSeniority());
    }

    public static AnalyzeRequestDto toDto(AnalysisContext domain) {
        if (domain == null) {
            return null;
        }
        return new AnalyzeRequestDto(domain.locale().toLanguageTag(), domain.targetRole(), domain.targetSeniority());
    }

    public static EducationItem toDomain(EducationDto dto) {
        if (dto == null) {
            return null;
        }
        return new EducationItem(
                dto.degree(),
                dto.institution(),
                parseYear(dto.start()),
                parseYear(dto.end()),
                dto.note()
        );
    }

    public static EducationDto toDto(EducationItem domain) {
        if (domain == null) {
            return null;
        }

        Integer startValue = null;
        if (domain.start() != null) {
            startValue = domain.start().getValue();
        }

        Integer endValue = null;
        if (domain.end() != null) {
            endValue = domain.end().getValue();
        }

        return new EducationDto(
                domain.degree(),
                domain.institution(),
                startValue,
                endValue,
                domain.note()
        );
    }

    public static List<EducationItem> toDomainEducationList(List<EducationDto> list) {
        if (list == null) {
            return List.of();
        }
        return list.stream().map(GlobalControllerMapper::toDomain).collect(Collectors.toList());
    }

    public static List<EducationDto> toDtoEducationList(List<EducationItem> list) {
        if (list == null) {
            return List.of();
        }
        return list.stream().map(GlobalControllerMapper::toDto).collect(Collectors.toList());
    }

    public static ExperienceItem toDomain(ExperienceDto dto) {
        if (dto == null) {
            return null;
        }
        return new ExperienceItem(
                dto.company(),
                dto.role(),
                parseYearMonth(dto.start()),
                parseYearMonth(dto.end()),
                dto.summary(),
                List.of()
        );
    }

    public static ExperienceDto toDto(ExperienceItem domain) {
        if (domain == null) {
            return null;
        }

        String start = null;
        if (domain.start() != null) {
            start = domain.start().toString();
        }

        String end = null;
        if (domain.end() != null) {
            end = domain.end().toString();
        }

        return new ExperienceDto(
                domain.company(),
                domain.role(),
                start,
                end,
                domain.summary()
        );
    }

    public static List<ExperienceItem> toDomainExperienceList(List<ExperienceDto> list) {
        if (list == null) {
            return List.of();
        }
        return list.stream().map(GlobalControllerMapper::toDomain).collect(Collectors.toList());
    }

    public static List<ExperienceDto> toDtoExperienceList(List<ExperienceItem> list) {
        if (list == null) {
            return List.of();
        }
        return list.stream().map(GlobalControllerMapper::toDto).collect(Collectors.toList());
    }

    public static GenerationContext toDomain(GenerateRequestDto dto) {
        if (dto == null) {
            return null;
        }
        return new GenerationContext(
                dto.fullName(),
                dto.targetRole(),
                dto.skills(),
                toDomainExperienceList(dto.experience()),
                toDomainEducationList(dto.education()),
                parseLocale(dto.locale())
        );
    }

    public static GenerateRequestDto toDto(GenerationContext domain) {
        if (domain == null) {
            return null;
        }
        return new GenerateRequestDto(
                domain.fullName(),
                domain.targetRole(),
                domain.skills(),
                toDtoExperienceList(domain.experience()),
                toDtoEducationList(domain.education()),
                domain.locale().toLanguageTag()
        );
    }

    public static AnalysisResultDto toDto(AnalysisResult domain) {
        if (domain == null) {
            return null;
        }
        return new AnalysisResultDto(
                domain.resumeId(),
                domain.recommendations(),
                domain.analyzedAt(),
                domain.analyzerVersion()
        );
    }

    public static GeneratedResumeDto toDto(GeneratedResume domain) {
        if (domain == null) {
            return null;
        }
        return new GeneratedResumeDto(
                domain.title(),
                domain.summary(),
                domain.skills(),
                domain.experience(),
                domain.education(),
                domain.templateVersion(),
                domain.locale()
        );
    }

    public static AnalysisResult toDomain(AnalysisResultDto dto) {
        if (dto == null) {
            return null;
        }
        return new AnalysisResult(
                dto.resumeId(),
                dto.recommendations(),
                dto.analyzedAt(),
                dto.analyzerVersion()
        );
    }

    public static GeneratedResume toDomain(GeneratedResumeDto dto) {
        if (dto == null) {
            return null;
        }
        return new GeneratedResume(
                dto.title(),
                dto.summary(),
                dto.skills(),
                dto.experience(),
                dto.education(),
                dto.templateVersion(),
                dto.locale()
        );
    }

    private static Locale parseLocale(String value) {
        if (Objects.isNull(value) || value.isBlank()) {
            return DEFAULT_LOCALE;
        }
        try {
            return Locale.forLanguageTag(value);
        } catch (Exception ignored) {
            return DEFAULT_LOCALE;
        }
    }

    private static Year parseYear(Integer value) {
        if (value == null || value < 1900 || value > Year.now().getValue() + 1) {
            return null;
        }
        try {
            return Year.of(value);
        } catch (Exception ignored) {
            return null;
        }
    }

    private static YearMonth parseYearMonth(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            return YearMonth.parse(value);
        } catch (Exception ignored) {
            return null;
        }
    }
}