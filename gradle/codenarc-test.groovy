ruleset {
    description 'Bloom Health Test Source CodeNarc Rules'

    ruleset('file:gradle/codenarc.groovy') {
        ClassJavadoc(enabled: false)
        MethodName (
            // From https://github.com/brian428/grails-coffeescript-compiler-plugin/blob/master/grails-app/conf/codenarc.groovy
            regex: /[a-z#\-][\w\s'\(\)#\-]*/
        )
        DuplicateNumberLiteral(enabled: false)
        DuplicateStringLiteral(enabled: false)
        DuplicateListLiteral(enabled: false)
        DuplicateMapLiteral(enabled: false)
    }
}