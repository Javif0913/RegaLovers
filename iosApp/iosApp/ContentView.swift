import SwiftUI
import Shared

struct ContentView: View {
    @State var questions: [QuestionDto] = []
    

    var body: some View {
        List(questions, id: \.id) { q in
            QuestionItem(questionDto: q)
        }
        .onAppear {
            let nsMutableArray = QuestionManager.shared.sampleQuestionList
            if let swiftArray = nsMutableArray as? [QuestionDto] {
                self.questions = swiftArray
            }

        }
    }
}

struct QuestionItem: View {
    let questionDto: QuestionDto
    
    var body: some View {
        VStack {
            Text(questionDto.text)
                .font(.title)
            Spacer().frame(height: 18)
            Text(questionDto.category)
        }
        .frame(maxWidth: .infinity,  alignment: .top)
        .padding()
        .background(Color.gray)
    }
    
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
