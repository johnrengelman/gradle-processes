ruleset {
    description 'Bloom Health CodeNarc Rules'

    ruleset('rulesets/basic.xml')
    ruleset('rulesets/braces.xml')
    ruleset('rulesets/concurrency.xml')
    ruleset('rulesets/convention.xml')
    ruleset('rulesets/design.xml')
    ruleset('rulesets/dry.xml')
    ruleset('rulesets/exceptions.xml')
    ruleset('rulesets/formatting.xml') {
        'SpaceAroundMapEntryColon' {
            characterAfterColonRegex = /\s/
        }
    }
    ruleset('rulesets/generic.xml')
    ruleset('rulesets/groovyism.xml')
    ruleset('rulesets/imports.xml')
    ruleset('rulesets/jdbc.xml')
    ruleset('rulesets/junit.xml')
    ruleset('rulesets/logging.xml')
    ruleset('rulesets/naming.xml')
    ruleset('rulesets/security.xml') {
        'JavaIoPackageAccess' enabled: false
    }
    ruleset('rulesets/size.xml') {
        'CrapMetric' enabled: false //This rule requires a coverage results file and isn't really helpful.
    }
    ruleset('rulesets/unnecessary.xml') {
        'UnnecessaryReturnKeyword' enabled: false
        'UnnecessaryPublicModifier' enabled: false
        'UnnecessarySubstring' enabled: false
    }
    ruleset('rulesets/unused.xml')
}
