
sys.props.get("plugin.version") match {
  case Some(x) => addSbtPlugin("com.fgrutsch" % "sbt-swagger-ui" % x)
  case _       => sys.error("""|The system property 'plugin.version' is not defined.
                         |Specify this property using the scriptedLaunchOpts -D.""".stripMargin)
}