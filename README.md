Domain-slash-Converter
======================

This is a simple framework that introduces two high level constructs to help organize programs into regions with distinct 'vocabularies.' The idea is to make regions of code having a shared 'separation of concerns' (on a level higher than Java classes) explicit; this is done by partitioning a program into a number of different 'Domains,' each of which have a specialized language for talking about whatever it is that it does. The idea is to emphasis language specialization for specific tasks, while adding as little burden as possible in doing so—the same idea as a domain specific language, without changing the grammar which is domain agnostic in most cases.

Converters come in to facilitate communication between domains; they are essentially translators of constructs expressed in the language of one domain into the language of another. For instance, let's say you have a domain for dealing with the strictly spatial properties of objects: their extent and position in space; additionally, you have a separate domain for dealing with appearances of physical objects. The first domain has data structures representing 'physical objects' whereas the second domain deals in 'renderables'; the converter, PhysicalObject2Renderable, just carries out the mapping of PhysicalObjects to Renderables. Each Domain has a reference to this shared Converter, so the source Domain can 'push' PhysicalObjects into it, or the destination Domain can 'pull' PhysicalObjects through it; in either case, they end up as Renderables on the other side. Implementing a Converter largely consists of implementing a 'convert' method that takes in an object from the source Domain and returns an object of the destination Domain.

The initial impetus for this design came largely from two ideas: we know that we should partition our projects into separate abstraction levels, but no languages (that I know of) have 'level of abstraction' as an explicit unit; and, Domain Specific Languages are not as lightweight as they could be since they bring specialized grammars in addition to specialized vocabularies, while a specialized vocabulary is (for most purposes) all that's needed. Domains can represent abstraction levels explicitly (as well as 'horizontally related' program partitions), and they encourage thinking in terms of a specialized vocabulary for some problem domain, without the cost of introducing a new language for each. Since thinking about this structure, I've also come to realize that the conversion/translation process that must occur when disparate domains communicate, is a special kind of beast on its own: it requires knowledge of the vocabulary of two domains, it's better suited to declarative description, and the translations/conversions often have common elements, so general purpose aids can be developed. Also, the queuing structure behind domain communication will make it easier to have code in separate domains running asynchronously.

I'm actively writing the first non-trivial test application on the framework at the moment. The application is for producing 3D visualizations of a wide range of abstract constructs, which can be 'fast-forwarded' and 're-winded,' and with a 'limited-attention' camera that moves around the scene automatically observing interesting things. It has been quite successful so far, the largest project I've worked on where I don't feel any slowdown after ammassing a significant bulk of code. That said it's given me a number of ideas for enhancing the framework that aren't yet reflected here.

A general argument for this structure as it relates to known values in software development runs as follows: 
If you have a program made up of regions, eaching having their own ‘concerns’—which I would characterize by their having a specialized vocabulary of functions, classes, variables—and these distinct regions need to communicate, the terms of one region must be translated into the terms of the other before such a communication can happen. This conversion/translation process requires the vocabulary of both areas, so it will be a region with (relatively) high coupling. This Domain/Converter framework is a way of being explicit about where the boundaries in your code are for a section using one ‘vocabulary,’ as well as a way of sequestering the translation activities that sit at the interface of two such demarcated regions.

Eventually, I'd like to work on a language based on the Domain/Converter concept, but for now, there's this.
