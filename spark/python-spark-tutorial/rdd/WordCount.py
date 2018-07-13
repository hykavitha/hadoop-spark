from pyspark import SparkContext, SparkConf

if __name__ == "__main__":
	conf = SparkConf().setAppName("word count").setMaster("local[3]")
	sc = SparkContext(conf = conf)
	lines = sc.textFile("/Users/KaviAnu/Documents/hadoop-code/spark/python-spark-tutorial/in/word_count.text")
    
	words = lines.flatMap(lambda line: line.split(" "))
	wordCounts = words.countByValue()
	i=0	
	for word, count in wordCounts.items():
		print("count is: {} ".format(1))
		print("{} : {}".format(word, count))
		i+=1
