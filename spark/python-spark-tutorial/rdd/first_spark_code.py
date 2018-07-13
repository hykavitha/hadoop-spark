import pyspark
sc = pyspark.SparkContext('local[*]')

#do something to prove it works
rdd = sc.parallelize(range(1000))
rdd.takeSample(False,5)
rdd.sumApprox(3)

