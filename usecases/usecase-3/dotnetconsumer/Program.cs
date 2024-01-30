using System;
using System.Text;
using Pulsar.Client.Api;
using Pulsar.Client.Common;
using System.Net.Http;
using System.Threading.Tasks;


    var topic_name = Environment.GetEnvironmentVariable("TOPIC_NAME");
    var tenant = Environment.GetEnvironmentVariable("TENANT");
    var namespace_name = Environment.GetEnvironmentVariable("NAMESPACE");
    var pulsar_host = Environment.GetEnvironmentVariable("PULSAR_HOST");

    // See https://aka.ms/new-console-template for more information
    Console.WriteLine($"listening topic pulsar://{tenant}/{namespace_name}/{topic_name}");
    var httpClient = new HttpClient();


    var client = await new PulsarClientBuilder()
                    .ServiceUrl($"pulsar://{pulsar_host}:6650")
                    .BuildAsync();

    var consumer = await client.NewConsumer()
         .Topic(topic_name)
         .SubscriptionName("my-subscription")
         .SubscribeAsync();

    while (true)
    {

       var msg = await consumer.ReceiveAsync();

        
       // fetch schema form pulsar schema registry
        var response = await httpClient.GetAsync($"http://{pulsar_host}:8080/admin/v2/schemas/{tenant}/{namespace_name}/{topic_name}/schema");
        var schema = await response.Content.ReadAsStringAsync();
        Console.WriteLine($"fetched schema is: {schema}");

        try
        {
            Console.WriteLine("received message: '{0}' ", System.Text.Encoding.Default.GetString(msg.GetValue()));
            await consumer.AcknowledgeAsync(msg.MessageId);
        }
        catch (Exception e)
        {
            Console.WriteLine("Unable to acknowledge message {0}", msg.MessageId);
            Console.WriteLine(e);
            await consumer.NegativeAcknowledge(msg.MessageId);
        }
    }