import AssemblyKeys._
name := "TwitterStream"
version := "1.0"
libraryDependencies ++= {
val sparkVer = "2.1.1"
Seq(
"org.apache.spark" %% "spark-core" % sparkVer,
"org.apache.spark" %% "spark-mllib" % sparkVer,
"org.apache.spark" %% "spark-streaming" % sparkVer,
"org.apache.spark" % "spark-streaming-kafka-0-10_2.11" % sparkVer,
"org.apache.kafka" % "kafka-clients" % "0.10.2.1"
)
}
scalaVersion := "2.11.8"
assemblySettings
mergeStrategy in assembly := {
case m if m.toLowerCase.endsWith("manifest.mf")      => MergeStrategy.discard
case m if m.toLowerCase.matches("meta-inf.*\\.sf$")  => MergeStrategy.discard
case "log4j.properties"                  => MergeStrategy.discard
case m if m.toLowerCase.startsWith("meta-inf/services/") => MergeStrategy.filterDistinctLines
case "reference.conf"                    => MergeStrategy.concat
case _                                   => MergeStrategy.first
}

